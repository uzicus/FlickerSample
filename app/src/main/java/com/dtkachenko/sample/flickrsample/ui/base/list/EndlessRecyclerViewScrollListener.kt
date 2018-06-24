package com.dtkachenko.sample.flickrsample.ui.base.list

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class EndlessRecyclerViewScrollListener(
        private val onLoadMore: (Unit) -> Unit,
        private val difference: Int = DEFAULT_DIFFERENCE
) : RecyclerView.OnScrollListener() {

    companion object {
        private const val DEFAULT_DIFFERENCE = 10
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val limit = recyclerView.layoutManager.itemCount
        val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (limit - lastVisibleItem < difference ) {
            onLoadMore.invoke(Unit)
        }
    }
}