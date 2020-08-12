package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.internal.RecyclerXRayApi

open class LocalRecyclerXRay internal constructor(
    xRayApiProvider: () -> RecyclerXRayApi
) {

    constructor(): this(XRayInitializer.xRayApiProvider)

    internal val xRayApi: RecyclerXRayApi by lazy(
        mode = LazyThreadSafetyMode.NONE,
        initializer = xRayApiProvider
    )

    // Internal state
    var settings: XRaySettings
        get() = xRayApi.settings
        set(value) { xRayApi.settings = value }

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