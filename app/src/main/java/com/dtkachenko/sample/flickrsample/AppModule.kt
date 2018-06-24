package com.dtkachenko.sample.flickrsample

import com.dtkachenko.sample.flickrsample.model.FlickrModel
import com.dtkachenko.sample.flickrsample.ui.photos.PhotosScreenPm
import com.dtkachenko.sample.flickrsample.ui.photoview.PhotoViewScreenPm
import org.koin.dsl.module.applicationContext

val appModule = applicationContext {

    factory { PhotosScreenPm(get(), get()) }
    factory { PhotoViewScreenPm() }

    bean { FlickrModel(get()) }

}