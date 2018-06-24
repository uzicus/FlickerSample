package com.dtkachenko.sample.flickrsample.system

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

val systemModule = applicationContext {
    bean { SearchRecentSuggestionsHelperImpl(androidApplication()) as SearchRecentSuggestionsHelper }
}