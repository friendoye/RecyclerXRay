package com.friendoye.recyclerxray.xray

import android.view.View
import android.view.ViewGroup

interface XRayDebugViewHolder {
    fun provideView(parent: ViewGroup): View
    fun bindView(debugView: View, result: XRayResult)
}
