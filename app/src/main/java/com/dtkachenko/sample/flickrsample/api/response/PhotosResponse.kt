package com.dtkachenko.sample.flickrsample.api.response

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
        @SerializedName("photos") val photos: PhotosPage,
        @SerializedName("stat") val status: Status,
        @SerializedName("message") val message: String
) {

    data class PhotosPage(
            @SerializedName("photo") val photos: List<Photo>
    ) {

        data class Photo(
                @SerializedName("id") val id: String,
                @SerializedName("owner") val owner: String,
                @SerializedName("title") val title: String,
                @SerializedName("server") val server: String,
                @SerializedName("secret") val secret: String
        )
    }
}