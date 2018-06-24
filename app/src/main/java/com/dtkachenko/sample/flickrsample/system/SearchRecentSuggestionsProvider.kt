package com.dtkachenko.sample.flickrsample.system

import android.content.SearchRecentSuggestionsProvider

class SearchRecentSuggestionsProvider : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "com.dtkachenko.sample.flickrsample.system.SearchRecentSuggestionsProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}