package com.dtkachenko.sample.flickrsample.system

import android.content.Context
import android.provider.SearchRecentSuggestions

class SearchRecentSuggestionsHelperImpl(context: Context) : SearchRecentSuggestionsHelper {

    private val suggestions = SearchRecentSuggestions(context,
                                                      SearchRecentSuggestionsProvider.AUTHORITY,
                                                      SearchRecentSuggestionsProvider.MODE)

    override fun saveRecentQuery(query: String) {
        suggestions.saveRecentQuery(query, null)
    }
}