package com.friendoye.recyclerxray

import android.view.View

val XRayResult.isViewVisibleForUser: Boolean
    get() = viewVisibility == View.VISIBLE && viewHeight > 0 && viewWidth > 0