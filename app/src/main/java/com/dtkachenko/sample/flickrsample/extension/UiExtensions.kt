package com.dtkachenko.sample.flickrsample.extension

import android.support.annotation.LayoutRes
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.view.actionViewEvents
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import io.reactivex.Observable


fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun SearchView.queryTextSubmit(): Observable<String> {
    return queryTextChangeEvents()
            .filter { it.isSubmitted }
            .map { it.queryText().toString() }
}

fun MenuItem.actionViewCollapsed(): Observable<Unit> {
    return actionViewEvents()
            .filter { it.menuItem().isActionViewExpanded }
            .map { Unit }

}