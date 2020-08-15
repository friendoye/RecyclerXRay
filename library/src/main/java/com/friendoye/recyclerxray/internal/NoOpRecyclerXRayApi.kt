package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRaySettings


@Suppress("SetterBackingFieldAssignment", "UNCHECKED_CAST")
internal object NoOpRecyclerXRayApi : RecyclerXRayApi {

    override var settings: XRaySettings = XRaySettings.Builder().build()
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