package com.dtkachenko.sample.flickrsample.ui.photos

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dtkachenko.sample.flickrsample.R
import com.dtkachenko.sample.flickrsample.entity.Photo
import com.dtkachenko.sample.flickrsample.extension.actionViewCollapsed
import com.dtkachenko.sample.flickrsample.extension.inflate
import com.dtkachenko.sample.flickrsample.extension.inject
import com.dtkachenko.sample.flickrsample.extension.queryTextSubmit
import com.dtkachenko.sample.flickrsample.ui.base.BaseScreen
import com.dtkachenko.sample.flickrsample.ui.base.list.BaseListAdapter
import com.dtkachenko.sample.flickrsample.ui.base.list.DiffItemsCallback
import com.dtkachenko.sample.flickrsample.ui.base.list.EndlessRecyclerViewScrollListener
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v4.widget.refreshing
import com.jakewharton.rxbinding2.view.visibility
import kotlinx.android.synthetic.main.item_photo.*
import kotlinx.android.synthetic.main.layout_progress_view.*
import kotlinx.android.synthetic.main.screen_photos.*

class PhotosScreen : BaseScreen<PhotosScreenPm>() {

    companion object {
        private const val PHOTOS_GRID_SPAN_COUNT = 2
    }

    private val photosAdapter = PhotosAdapter()
    private val photosDiff = PhotosDiff()

    override val pm: PhotosScreenPm by inject()
    override val screenLayout = R.layout.screen_photos

    override fun onInitView(view: View, savedViewState: Bundle?) {
        toolbar.inflateMenu(R.menu.menu_photos)

        val searchItem = toolbar.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        with(searchView) {
            val searchManager = (activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager)
            val searchableInfo = searchManager.getSearchableInfo(activity!!.componentName)

            isFocusable = false
            setIconifiedByDefault(false)
            setSearchableInfo(searchableInfo)

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int) = true

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = suggestionsAdapter.getItem(position) as Cursor
                    val index = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
                    val text = cursor.getString(index)

                    setQuery(text, true)

                    return true
                }
            })

            setOnQueryTextFocusChangeListener { v, hasFocus ->
                if (hasFocus.not() && query.isEmpty()) {
                    searchItem.collapseActionView()
                }
            }
        }

        with(recyclerView) {
            layoutManager = GridLayoutManager(context, PHOTOS_GRID_SPAN_COUNT).apply {
                spanSizeLookup = BaseListAdapter.HeaderFooterSpanSizeLookup(photosAdapter, PHOTOS_GRID_SPAN_COUNT)
            }

            addOnScrollListener(EndlessRecyclerViewScrollListener(presentationModel.nextPageAction.consumer::accept))
            adapter = photosAdapter
        }
    }

    override fun onBindPresentationModel(view: View, pm: PhotosScreenPm) {
        val searchItem = toolbar.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        pm.isLoading.observable bindTo progressBar.visibility()
        pm.isRefreshing.observable bindTo swipeRefreshLayout.refreshing()
        pm.photos.observable.bindTo { photosAdapter.updateItems(it, photosDiff) }
        pm.pageIsLoading.observable bindTo photosAdapter.progressView.visibility()
        pm.clearFocus.observable.bindTo { searchView.clearFocus() }

        swipeRefreshLayout.refreshes() bindTo pm.refreshAction.consumer
        searchView.queryTextSubmit() bindTo pm.searchQueryTextSubmit.consumer
        searchItem.actionViewCollapsed() bindTo pm.searchCollapsed.consumer
    }

    class PhotosDiff : DiffItemsCallback<Photo> {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = areItemsTheSame(oldItem, newItem)
    }

    inner class PhotosAdapter : BaseListAdapter<Photo, PhotosAdapter.PhotoViewHolder>(
            footerLayoutRes = R.layout.footer_paging_progress
    ) {

        val progressView get() = footerView!!

        override fun newViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            return PhotoViewHolder(parent.inflate(R.layout.item_photo))
        }

        inner class PhotoViewHolder(itemView: View) : BaseViewHolder<Photo>(itemView) {

            init {
                itemView.setOnClickListener { item passTo presentationModel.itemClicks.consumer }
            }

            override fun bind(item: Photo) {
                val glide = Glide.with(activity!!)

                val options = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .centerCrop()
                        .placeholder(R.drawable.bg_photo_placeholder)

                val thumbnail = glide
                        .asBitmap()
                        .load(item.thumbnailUrl)
                        .transition(BitmapTransitionOptions().crossFade())
                        .apply(options)


                glide
                        .asBitmap()
                        .load(item.mediumUrl)
                        .transition(BitmapTransitionOptions().crossFade())
                        .thumbnail(thumbnail)
                        .apply(options)
                        .into(photoImage)
            }
        }
    }
}