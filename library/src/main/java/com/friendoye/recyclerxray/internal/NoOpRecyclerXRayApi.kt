package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRaySettings

@Suppress("SetterBackingFieldAssignment", "UNCHECKED_CAST")
internal class NoOpRecyclerXRayApi(
    defaultSettings: XRaySettings
) : RecyclerXRayApi {

    @Suppress("UNUSED_PARAMETER")
    override var settings: XRaySettings = defaultSettings
        set(value) {}

    override fun toggleSecrets() {
        // No-op
    }

    override fun showSecrets() {
        // No-op
    }

    override fun hideSecrets() {
        // No-op
    }

    override fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(adapter: T): RecyclerView.Adapter<VH> {
        return adapter
    }

    override fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(
        adapter: T,
        settings: XRaySettings
    ): RecyclerView.Adapter<VH> {
        return adapter
    }

    override fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(adapter: RecyclerView.Adapter<*>): T {
        return adapter as T
    }
}
