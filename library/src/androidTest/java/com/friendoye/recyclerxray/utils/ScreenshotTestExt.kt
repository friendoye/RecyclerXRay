package com.friendoye.recyclerxray.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.karumi.shot.ScreenshotTest

fun ScreenshotTest.compareRecyclerScreenshot(
    recyclerView: RecyclerView,
    heightInPx: Int? = null,
    widthInPx: Int? = null,
    name: String? = null
) {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    compareScreenshot(
        recyclerView,
        heightInPx ?: recyclerView.height,
        widthInPx ?: recyclerView.width,
        name
    )
}
