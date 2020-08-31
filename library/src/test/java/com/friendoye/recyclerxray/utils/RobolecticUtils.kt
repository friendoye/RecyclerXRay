package com.friendoye.recyclerxray.utils

import android.os.Looper.getMainLooper
import org.robolectric.Shadows.shadowOf

/**
 * Since Robolectric 4.4 all messages are post to Looper async, no sync as was before.
 * To overcome this, use this method to wait until all messages are executed.
 */
fun idleLooper() {
    shadowOf(getMainLooper()).idle()
}
