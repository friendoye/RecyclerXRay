package com.friendoye.recyclerxray.sample

import android.app.Application
import com.friendoye.recyclerxray.XRayInitializer

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        XRayInitializer.init(this)
    }
}
