package com.friendoye.recyclerxray.xray

import androidx.annotation.ColorInt


data class XRayResult(
    val viewHolderClass: Class<*>,
    val viewHolderType: Int,
    @ColorInt val color: Int
)