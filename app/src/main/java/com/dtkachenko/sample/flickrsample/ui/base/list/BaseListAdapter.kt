package com.dtkachenko.sample.flickrsample.ui.base.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.dtkachenko.sample.flickrsample.extension.inflate
import kotlinx.android.extensions.LayoutContainer

abstract class BaseListAdapter<T, VH>(
        private val headerLayoutRes: Int? = null,
        private val footerLayoutRes: Int? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
        where VH : RecyclerView.ViewHolder,
              VH : Bindable<T> {

    companion object {
        private const val HEADER_VIEW_TYPE = 1
        private const val FOOTER_VIEW_TYPE = 2
        private const val ITEM_VIEW_TYPE = 3
    }

    private var items: MutableList<T> = mutableListOf()

    private val headerOffset: Int get() = if (headerView != null) 1 else 0
    private val footerOffset: Int get() = if (footerView != null) 1 else 0

    var headerView: View? = null
        private set(value) {
            field = value
            notifyItemInserted(0)
        }

    var footerView: View? = null
        private set(value) {
            field = value
            if (field == null) {
                notifyItemRemoved(itemCount - 1)
            } else {
                notifyItemInserted(itemCount - 1)
            }
        }

    override fun getItemCount() = items.size

    fun getItem(position: Int) = items[position]

    fun getItems() = items

    fun setItems(list: List<T>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItems(list: List<T>, diffCallback: DiffItemsCallback<T>) {
        if (items.isEmpty()) {
            setItems(list)
        } else {
            val diffResult = DiffUtil.calculateDiff(DiffCallback(items, list, diffCallback))
            items.clear()
            items.addAll(list)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (headerLayoutRes != null) {
            headerView = recyclerView.inflate(headerLayoutRes)
        }

        if (footerLayoutRes != null) {
            footerView = recyclerView.inflate(footerLayoutRes)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        headerView = null
        footerView = null
    }

    override fun getItemViewType(position: Int): Int {
        return if (headerView != null && position == 0) {
            HEADER_VIEW_TYPE
        } else if (footerView != null && position == itemCount - 1) {
            FOOTER_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(headerView!!)
            FOOTER_VIEW_TYPE -> FooterViewHolder(footerView!!)
            else -> newViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (headerView != null && position == 0) return

        if (footerView != null && position == itemCount - 1) return

        @Suppress("UNCHECKED_CAST")
        (holder as VH).bind(getItem(position))

    }

    abstract fun newViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract inner class BaseViewHolder<T>(override val containerView: View) :
            RecyclerView.ViewHolder(containerView),
            Bindable<T>,
            LayoutContainer {

        val item get() = getItem(adapterPosition)
        val resources get() = itemView.resources!!
    }

    private class HeaderViewHolder(headerView: View) : RecyclerView.ViewHolder(headerView)
    private class FooterViewHolder(footerView: View) : RecyclerView.ViewHolder(footerView)

    class HeaderFooterSpanSizeLookup(
            private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
            private val spanCount: Int
    ) : GridLayoutManager.SpanSizeLookup() {

        override fun getSpanSize(position: Int): Int {
            return when (adapter.getItemViewType(position)) {
                BaseListAdapter.ITEM_VIEW_TYPE -> 1
                else -> spanCount
            }
        }

    }
}