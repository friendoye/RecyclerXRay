package com.friendoye.recyclerxray

interface Bindable<T : Any> {
    fun bind(item: T) = Unit
}