package com.friendoye.recyclerxray.sample


sealed class ItemType {
    class Small(val number: Int) : ItemType()
    class Large(val string: String) : ItemType()
    object Widest : ItemType()
}