package com.friendoye.recyclerxray.sample

import com.friendoye.recyclerxray.sample.InnerItemType.Box
import com.friendoye.recyclerxray.sample.InnerItemType.Circle
import com.friendoye.recyclerxray.sample.ItemType.*


val SAMPLE_DATA_FULL = listOf(
    Small(1), Small(2), Small(3),
    Large("One"), Small(4),
    Empty,
    Widest,
    Empty
)

val SAMPLE_DATA_PARTIAL = listOf(
    Small(1), // Small(2), Small(3),
    Large("One"), // Small(4),
    Empty,
    Widest
    // Empty
)

val LOCAL_SAMPLE_DATA_FULL = listOf(
    Small(5), Large("Two"),
    Small(6), Small(7), Small(8),
    Widest,
    Small(9), Small(10), Small(11),
    Large("Three"), Small(12)
)

val LOCAL_SAMPLE_DATA_PARTIAL = listOf(
    /*Small(5),*/ Large("Two"),
    Small(6), // Small(7), Small(8),
    Widest,
    /*Small(9),*/ Small(10), // Small(11),
    Large("Three") // , Small(12)
)

val INNER_DATA_FULL = listOf(
    HorizontalRecycler(
        listOf(Box, Circle, Circle, Box, Box, Circle, Box)
    )
)

val INNER_DATA_PARTIAL = INNER_DATA_FULL

val INNER_DATA_ADAPTER_CHANGE_FULL = listOf(
    HorizontalRecycler(
        listOf(Box, Circle, Box, Box, Box, Circle, Box),
        changeAdapter = true
    )
)

val INNER_DATA_ADAPTER_CHANGE_PARTIAL = listOf(
    HorizontalRecycler(
        listOf(Box, Circle, Box, Circle),
        changeAdapter = true
    )
)