package com.friendoye.recyclerxray.sample

interface Bindable<T : Any> {
    fun bind(item: T) = Unit
}