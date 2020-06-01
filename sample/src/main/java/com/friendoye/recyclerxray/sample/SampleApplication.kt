package com.friendoye.recyclerxray.sample

import android.app.Application
import com.friendoye.recyclerxray.RecyclerXRay


class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RecyclerXRay.init(this)
    }
}