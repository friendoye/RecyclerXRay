package com.friendoye.recyclerxray.sample.shared

import android.app.Application
import com.friendoye.recyclerxray.XRayInitializer

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        XRayInitializer.init(this)
    }
}
