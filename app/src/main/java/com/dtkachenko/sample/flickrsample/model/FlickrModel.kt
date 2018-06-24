package com.dtkachenko.sample.flickrsample.model

import com.dtkachenko.sample.flickrsample.api.FlickrApi
import com.dtkachenko.sample.flickrsample.api.response.PhotosResponse
import com.dtkachenko.sample.flickrsample.entity.Photo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FlickrModel(
        private val api: FlickrApi
) {

    fun getRecentPhotos(page: Int = 1, pageSize: Int): Single<List<Photo>> {
        return api.getRecent(pageSize, page)
                .subscribeOn(Schedulers.io())
                .map {
                    it.photos.photos.map {
                        it.mapToPhoto()
                    }
                }
    }

    fun searchPhotos(query: String, page: Int = 1, pageSize: Int): Single<List<Photo>> {
        return api.search(pageSize, page, query)
                .subscribeOn(Schedulers.io())
                .map {
                    it.photos.photos.map {
                        it.mapToPhoto()
                    }
                }
    }

    private fun PhotosResponse.PhotosPage.Photo.mapToPhoto() = Photo(
            id = id,
            thumbnailUrl = "https://farm1.staticflickr.com/$server/${id}_${secret}_t.jpg",
            mediumUrl = "https://farm1.staticflickr.com/$server/${id}_$secret.jpg",
            largeUrl = "https://farm1.staticflickr.com/$server/${id}_${secret}_b.jpg"
    )
}