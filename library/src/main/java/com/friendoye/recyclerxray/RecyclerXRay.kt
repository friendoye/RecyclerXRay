package com.friendoye.recyclerxray

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.internal.NoOpRecyclerXRayApi
import com.friendoye.recyclerxray.internal.NotInitializedRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RealRecyclerXRayApi
import com.friendoye.recyclerxray.internal.RecyclerXRayApi


@Suppress("SimplifyBooleanWithConstants")
object RecyclerXRay {

    // Global settings
    internal var xRayApi: RecyclerXRayApi = NotInitializedRecyclerXRayApi

    // Internal state
    var settings: XRaySettings
        get() = xRayApi.settings
        set(value) { xRayApi.settings = value }

    fun init(context: Context) {
        val app = context.applicationContext as Application
        val isAppDebuggable = (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        init(context, isNoOpMode = !isAppDebuggable)
    }

    fun init(context: Context, isNoOpMode: Boolean) {
        if (xRayApi !is NotInitializedRecyclerXRayApi) {
            throw IllegalStateException("RecyclerXRay is already initialized.")
        }

        xRayApi = if (isNoOpMode) {
            NoOpRecyclerXRayApi
        } else {
            RealRecyclerXRayApi()
        }
    }

    @VisibleForTesting
    internal fun reset() {
        xRayApi = NotInitializedRecyclerXRayApi
    }

    fun toggleSecrets() = xRayApi.toggleSecrets()

    fun showSecrets() = xRayApi.showSecrets()

    fun hideSecrets() = xRayApi.hideSecrets()

    fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(
        adapter: T
    ): RecyclerView.Adapter<VH> = xRayApi.wrap(adapter)

    fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(
        adapter: RecyclerView.Adapter<*>
    ): T = xRayApi.unwrap(adapter)

}