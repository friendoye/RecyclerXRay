package com.friendoye.recyclerxray

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.annotation.VisibleForTesting
import com.friendoye.recyclerxray.internal.NoOpRecyclerXRayApi
import com.friendoye.recyclerxray.internal.NotInitializedRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RecyclerXRayApi

/**
 * Main purpose of [XRayInitializer] - allow user to control, whether
 * no-op or real implementation should be used in all [LocalRecyclerXRay] during app session.
 *
 * User should invoke any of [XRayInitializer]'s init() methods. Otherwise,
 * invoking any of `LocalRecyclerXRay` method, will trigger [RecyclerXRayIsNotInitializedException].
 */
object XRayInitializer {

    internal var NOT_INITIALIZED_PROVIDER: () -> RecyclerXRayApi = { NotInitializedRecyclerXRayApi }
    internal var NO_OP_PROVIDER: (XRaySettings) -> RecyclerXRayApi = ::NoOpRecyclerXRayApi

    internal var xRayApiProvider: () -> RecyclerXRayApi = NOT_INITIALIZED_PROVIDER
    internal var isInitialized = false

    /**
     * Depending on whether [Application] is debuggable or not, sets real or
     * no-op implementation for all [LocalRecyclerXRay].
     *
     * If debuggable - real implementation, not debuggable - no-op implementation.
     */
    @JvmOverloads
    fun init(context: Context,
             defaultXRaySettings: XRaySettings = XRaySettings.Builder().build()) {
        val app = context.applicationContext as Application
        val isAppDebuggable = (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        init(isNoOpMode = !isAppDebuggable, defaultXRaySettings = defaultXRaySettings)
    }

    /**
     * Sets real or no-op implementation for all [LocalRecyclerXRay].
     */
    @JvmOverloads
    fun init(isNoOpMode: Boolean = false,
             defaultXRaySettings: XRaySettings = XRaySettings.Builder().build()) {
        if (isInitialized) {
            throw IllegalStateException("RecyclerXRay is already initialized.")
        }

        xRayApiProvider = if (isNoOpMode) {
            { NO_OP_PROVIDER(defaultXRaySettings) }
        } else {
            { RealRecyclerXRayApi(defaultXRaySettings) }
        }

        isInitialized = true
    }

    @VisibleForTesting
    internal fun init(xRayApiProvider: () -> RecyclerXRayApi) {
        init(isNoOpMode = false)
        this.xRayApiProvider = xRayApiProvider
    }

    @VisibleForTesting
    internal fun reset() {
        xRayApiProvider = NOT_INITIALIZED_PROVIDER
        isInitialized = false
    }
}