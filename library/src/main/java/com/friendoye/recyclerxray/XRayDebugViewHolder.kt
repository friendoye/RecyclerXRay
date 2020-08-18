package com.friendoye.recyclerxray

import android.view.View
import android.view.ViewGroup

/**
 * Provides a way to control which views will be showed in inspection mode
 * instead of original ViewHolder's views.
 */
interface XRayDebugViewHolder {
    /** Creates debug view for inspection. */
    fun provideView(parent: ViewGroup): View

    /** Binds inspection information for created debug view. */
    fun bindView(debugView: View, result: XRayResult)

    /**
     * In case, when view in ViewHolder has no visible rect on the screen,
     * click event for debug view will be handled by this method.
     * By default it does nothing.
     */
    fun onEmptyViewClick(debugView: View, result: XRayResult) = Unit
}
