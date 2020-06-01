package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRaySettings


@Suppress("SetterBackingFieldAssignment")
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
}