package com.friendoye.recyclerxray


enum class ItemType {
    SMALL,
    LARGE,
    WIDEST;

    companion object {
        fun fromOrdinal(ordinal: Int): ItemType? = values().getOrNull(ordinal)
    }
}