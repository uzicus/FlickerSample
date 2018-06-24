package com.dtkachenko.sample.flickrsample.api

import com.dtkachenko.sample.flickrsample.BuildConfig
import com.dtkachenko.sample.flickrsample.api.response.PhotosResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FlickrApi {

    companion object {
        private const val GET_RECENT_METHOD = "flickr.photos.getRecent"
        private const val SEARCH_METHOD = "flickr.photos.search"

        private val DEFAULT_OPTIONS = mapOf(
                "format" to "json",
                "nojsoncallback" to "1",
                "api_key" to BuildConfig.FLICKR_KEY
        )
    }

    @GET("rest")
    fun getRecent(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int,
            @Query("method") method: String = GET_RECENT_METHOD,
            @QueryMap defaultOptions: Map<String, String> = DEFAULT_OPTIONS
    ): Single<PhotosResponse>

    @GET("rest")
    fun search(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int,
            @Query("text") text: String,
            @Query("method") method: String = SEARCH_METHOD,
            @QueryMap defaultOptions: Map<String, String> = DEFAULT_OPTIONS
    ): Single<PhotosResponse>
}