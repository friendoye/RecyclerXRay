package com.friendoye.recyclerxray.sample


sealed class ItemType {
    data class Small(val number: Int) : ItemType()
    data class Large(val string: String) : ItemType()
    object Widest : ItemType()
    object Empty : ItemType()
}