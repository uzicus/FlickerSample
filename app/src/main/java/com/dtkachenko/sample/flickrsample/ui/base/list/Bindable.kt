package com.dtkachenko.sample.flickrsample.ui.base.list

interface Bindable<in T> {
    fun bind(item: T)
}