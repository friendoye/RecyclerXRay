package com.friendoye.recyclerxray.sample.groupie

val SAMPLE_DATA_FULL = listOf(
    SmallItem(1), SmallItem(2), SmallItem(3),
    LargeItem("One"), SmallItem(4),
    EmptyItem(),
    WidestItem(),
    EmptyItem()
)

val SAMPLE_DATA_PARTIAL = listOf(
    SmallItem(1), // SmallItem(2), SmallItem(3),
    LargeItem("One"), // Small(4),
    EmptyItem(),
    WidestItem()
    // EmptyItem()
)