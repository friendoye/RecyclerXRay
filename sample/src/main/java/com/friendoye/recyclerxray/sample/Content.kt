package com.friendoye.recyclerxray.sample

import com.friendoye.recyclerxray.sample.ItemType.*


val SAMPLE_DATA_FULL = listOf(
    Small(1), Small(2), Small(3),
    Large("One"), Small(4),
    Widest,
    Small(5), Large("Two"),
    Small(6), Small(7), Small(8),
    Widest,
    Small(9), Small(10), Small(11),
    Large("Three"), Small(12)
)

val SAMPLE_DATA_PARTIAL = listOf(
    Small(1), // Small(2), Small(3),
    Large("One"), // Small(4),
    Widest,
    /*Small(5),*/ Large("Two"),
    Small(6), // Small(7), Small(8),
    Widest,
    /*Small(9),*/ Small(10), // Small(11),
    Large("Three") // , Small(12)
)