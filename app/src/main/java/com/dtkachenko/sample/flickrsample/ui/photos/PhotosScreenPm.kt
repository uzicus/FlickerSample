package com.dtkachenko.sample.flickrsample.ui.photos

import com.dtkachenko.sample.flickrsample.entity.Photo
import com.dtkachenko.sample.flickrsample.model.FlickrModel
import com.dtkachenko.sample.flickrsample.system.SearchRecentSuggestionsHelper
import com.dtkachenko.sample.flickrsample.ui.OpenPhotoViewMessage
import com.dtkachenko.sample.flickrsample.ui.base.BaseScreenPm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.withLatestFrom
import me.dmdev.rxpm.bindProgress
import me.dmdev.rxpm.skipWhileInProgress

class PhotosScreenPm(
        private val flickrModel: FlickrModel,
        private val searchRecentSuggestionsHelper: SearchRecentSuggestionsHelper
) : BaseScreenPm() {

    companion object {
        const val PAGE_SIZE = 10
    }

    private val refreshing = State(false)
    private val searchQuery = State("")

    val isLoading = State<Boolean>()
    val isRefreshing = State<Boolean>()
    val pageIsLoading = State<Boolean>()

    val photos = State(emptyList<Photo>())

    val refreshAction = Action<Unit>()
    val nextPageAction = Action<Unit>()
    val itemClicks = Action<Photo>()
    val searchQueryTextSubmit = Action<String>()
    val searchCollapsed = Action<Unit>()

    val clearFocus = Command<Unit>()

    override fun onCreate() {

        searchCollapsed.observable
                .subscribe { searchQueryTextSubmit.consumer.accept("") }
                .untilDestroy()

        searchQueryTextSubmit.observable
                .filter { it != searchQuery.value }
                .subscribe {
                    searchRecentSuggestionsHelper.saveRecentQuery(it)
                    clearFocus.consumer.accept(Unit)
                    searchQuery.consumer.accept(it)
                    photos.consumer.accept(emptyList())
                    refreshAction.consumer.accept(Unit)
                }
                .untilDestroy()

        refreshAction.observable
                .skipWhileInProgress(refreshing.observable)
                .withLatestFrom(searchQuery.observable)
                .flatMapSingle { (_, query) ->
                    val loadFirstPage = if (query.isEmpty()) {
                        flickrModel.getRecentPhotos(pageSize = PAGE_SIZE)
                    } else {
                        flickrModel.searchPhotos(query = query, pageSize = PAGE_SIZE)
                    }

                    return@flatMapSingle loadFirstPage
                            .bindProgress(refreshing.consumer)
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .retry()
                .subscribe(photos.consumer)
                .untilDestroy()

        nextPageAction.observable
                .skipWhileInProgress(pageIsLoading.observable)
                .skipWhileInProgress(refreshing.observable)
                .withLatestFrom(photos.observable, searchQuery.observable)
                .flatMapSingle { (_, photos, query) ->
                    val nextPageNumber = photos.size / PAGE_SIZE + 1

                    val loadNextPage = if (query.isEmpty()) {
                        flickrModel.getRecentPhotos(page = nextPageNumber, pageSize = PAGE_SIZE)
                    } else {
                        flickrModel.searchPhotos(query = query, page = nextPageNumber, pageSize = PAGE_SIZE)
                    }

                    return@flatMapSingle loadNextPage
                            .bindProgress(pageIsLoading.consumer)
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { nextPage -> photos + nextPage }
                }
                .retry()
                .subscribe(photos.consumer)
                .untilDestroy()

        refreshing.observable
                .withLatestFrom(photos.observable)
                .subscribe { (refreshing, photos) ->
                    isLoading.consumer.accept(refreshing && photos.isEmpty())
                    isRefreshing.consumer.accept(refreshing && photos.isNotEmpty())
                }
                .untilDestroy()

        itemClicks.observable
                .subscribe { sendMessage(OpenPhotoViewMessage(it.largeUrl)) }
                .untilDestroy()

        // startup
        refreshAction.consumer.accept(Unit)
    }
}