package com.friendoye.recyclerxray

import androidx.annotation.ColorInt


data class XRayResult(
    val viewHolderClass: Class<*>,
    val viewHolderType: Int,
    @ColorInt val color: Int
)