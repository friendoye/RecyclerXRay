package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.RecyclerXRayIsNotInitializedException
import com.friendoye.recyclerxray.XRaySettings


internal object NotInitializedRecyclerXRayApi : RecyclerXRayApi {

    override var settings: XRaySettings
        get() = fireNotInitializedException()
        set(value) {
            fireNotInitializedException()
        }

    override fun toggleSecrets() {
        fireNotInitializedException()
    }

    override fun showSecrets() {
        fireNotInitializedException()
    }

    override fun hideSecrets() {
        fireNotInitializedException()
    }

    override fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(adapter: T): RecyclerView.Adapter<VH> {
        fireNotInitializedException()
    }

    override fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(
        adapter: T,
        settings: XRaySettings
    ): RecyclerView.Adapter<VH> {
        fireNotInitializedException()
    }

    override fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(adapter: RecyclerView.Adapter<*>): T {
        fireNotInitializedException()
    }

    private fun fireNotInitializedException(): Nothing = throw RecyclerXRayIsNotInitializedException()
}