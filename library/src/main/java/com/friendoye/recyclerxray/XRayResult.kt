package com.friendoye.recyclerxray

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView


data class XRayResult(
    val viewHolderClass: Class<out RecyclerView.ViewHolder>,
    val viewHolderType: Int,
    @ColorInt val color: Int,
    val customParams: Map<String, Any?>,
    val viewWidth: Int,
    val viewHeight: Int,
    val viewVisibility: Int
)