package com.friendoye.recyclerxray

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.annotation.VisibleForTesting
import com.friendoye.recyclerxray.internal.NoOpRecyclerXRayApi
import com.friendoye.recyclerxray.internal.NotInitializedRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RecyclerXRayApi

object XRayInitializer {

    private val NOT_INITIALIZED_PROVIDER: () -> RecyclerXRayApi = { NotInitializedRecyclerXRayApi }
    private val NO_OP_PROVIDER: () -> RecyclerXRayApi = { NoOpRecyclerXRayApi }

    internal var xRayApiProvider: () -> RecyclerXRayApi = NOT_INITIALIZED_PROVIDER
    internal var isInitialized = false

    fun init(context: Context) {
        val app = context.applicationContext as Application
        val isAppDebuggable = (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        init(context, isNoOpMode = !isAppDebuggable)
    }

    fun init(context: Context, isNoOpMode: Boolean) {
        if (isInitialized) {
            throw IllegalStateException("RecyclerXRay is already initialized.")
        }

        xRayApiProvider = if (isNoOpMode) {
            NO_OP_PROVIDER
        } else {
            ::RealRecyclerXRayApi
        }

        isInitialized = true
    }

    @VisibleForTesting
    internal fun reset() {
        xRayApiProvider = NOT_INITIALIZED_PROVIDER
        isInitialized = false
    }
}