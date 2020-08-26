package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.internal.RecyclerXRayApi

/**
 * [LocalRecyclerXRay] can control visual inspection mode for a given wrapped
 * [RecyclerView.Adapter].
 *
 * Example of usage:
 *
 *     val localRecyclerXRay = LocalRecyclerXRay()
 *     val wrappedAdapter = localRecyclerXRay.wrap(adapter)
 *     recyclerView.adapter = wrappedAdapter
 *
 *     localRecyclerXRay.showSecrets()
 *
 */
open class LocalRecyclerXRay internal constructor(
    xRayApiProvider: () -> RecyclerXRayApi
) {

    constructor() : this(XRayInitializer.xRayApiProvider)

    internal val xRayApi: RecyclerXRayApi by lazy(
        mode = LazyThreadSafetyMode.NONE,
        initializer = xRayApiProvider
    )

    /**
     * Current settings for given [LocalRecyclerXRay].
     */
    var settings: XRaySettings
        get() = xRayApi.settings
        set(value) { xRayApi.settings = value }

    /**
     * Changes inspection mode state to opposite.
     */
    fun toggleSecrets() = xRayApi.toggleSecrets()

    /**
     * Starts inspection mode.
     */
    fun showSecrets() = xRayApi.showSecrets()

    /**
     * Stops inspection mode.
     */
    fun hideSecrets() = xRayApi.hideSecrets()

    /**
     * Registers provided adapter for future inspection.
     *
     * @return a wrapped adapter, that should be provided to [RecyclerView].
     */
    fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(
        adapter: T
    ): RecyclerView.Adapter<VH> = xRayApi.wrap(adapter)

    internal fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(
        adapter: T,
        settings: XRaySettings
    ): RecyclerView.Adapter<VH> = xRayApi.wrap(adapter, settings)

    /**
     * Convenient method to retrieve original adapter, that was wrapped before.
     *
     * @return an unwrapped adapter, that was decorated by provided adapter.
     */
    fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(
        adapter: RecyclerView.Adapter<*>
    ): T = xRayApi.unwrap(adapter)
}
