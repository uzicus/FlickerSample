package com.dtkachenko.sample.flickrsample.api.response

import com.google.gson.annotations.SerializedName

enum class Status {
    @SerializedName("fail") FAIL,
    @SerializedName("ok") OK
}