package com.friendoye.recyclerxray.sample.epoxy

import com.friendoye.recyclerxray.sample.shared.ItemType.Empty
import com.friendoye.recyclerxray.sample.shared.ItemType.Large
import com.friendoye.recyclerxray.sample.shared.ItemType.Small
import com.friendoye.recyclerxray.sample.shared.ItemType.Widest

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
