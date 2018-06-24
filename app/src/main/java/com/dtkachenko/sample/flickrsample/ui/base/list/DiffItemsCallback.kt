package com.dtkachenko.sample.flickrsample.ui.base.list

interface DiffItemsCallback<in T> {
    fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    fun areContentsTheSame(oldItem: T, newItem: T): Boolean
}