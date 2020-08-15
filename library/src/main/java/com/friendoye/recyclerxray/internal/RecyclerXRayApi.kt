package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRaySettings


internal interface RecyclerXRayApi {
    var settings: XRaySettings

    fun toggleSecrets()
    fun showSecrets()
    fun hideSecrets()
    fun <T : RecyclerView.Adapter<VH>, VH: RecyclerView.ViewHolder> wrap(adapter: T): RecyclerView.Adapter<VH>
    fun <T : RecyclerView.Adapter<VH>, VH: RecyclerView.ViewHolder> wrap(adapter: T, settings: XRaySettings): RecyclerView.Adapter<VH>
    fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(adapter: RecyclerView.Adapter<*>): T
}