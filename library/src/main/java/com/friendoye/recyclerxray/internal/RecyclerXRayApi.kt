package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.ScannableRecyclerAdapter
import com.friendoye.recyclerxray.XRaySettings
import com.friendoye.recyclerxray.asWeakRef


internal interface RecyclerXRayApi {
    var settings: XRaySettings

    fun toggleSecrets()
    fun showSecrets()
    fun hideSecrets()
    fun <T : RecyclerView.Adapter<VH>, VH: RecyclerView.ViewHolder> wrap(adapter: T): RecyclerView.Adapter<VH>
}