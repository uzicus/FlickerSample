package com.dtkachenko.sample.flickrsample.ui.base.list

import android.support.v7.util.DiffUtil

class DiffCallback<in T>(
        private val oldList: List<T>,
        private val newList: List<T>,
        private val diffItemsCallback: DiffItemsCallback<T>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return diffItemsCallback.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return diffItemsCallback.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }
}