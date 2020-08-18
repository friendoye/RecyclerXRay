package com.friendoye.recyclerxray

import android.view.View

/**
 * Indicates, whether original [ViewHolder]'s itemView is physically viewable on phone screen.
 */
val XRayResult.isViewVisibleForUser: Boolean
    get() = viewVisibility == View.VISIBLE && viewHeight > 0 && viewWidth > 0