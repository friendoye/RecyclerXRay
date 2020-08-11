package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.*
import java.lang.ref.WeakReference


internal class RealRecyclerXRayApi : RecyclerXRayApi {

    // Internal state
    private val adapters: MutableSet<WeakReference<RecyclerView.Adapter<*>>> = mutableSetOf()
    internal var isInXRayMode = false
    override var settings: XRaySettings = XRaySettings.Builder().build()

    override fun toggleSecrets() {
        if (isInXRayMode) {
            hideSecrets()
        } else {
            showSecrets()
        }
    }

    override fun showSecrets() {
        if (!isInXRayMode) {
            isInXRayMode = true
            updateAdapters()
        }
    }

    override fun hideSecrets() {
        if (isInXRayMode) {
            isInXRayMode = false
            updateAdapters()
        }
    }

    override fun <T : RecyclerView.Adapter<VH>, VH: RecyclerView.ViewHolder> wrap(adapter: T): RecyclerView.Adapter<VH> {
        adapters.add(adapter.asWeakRef())
        return ScannableRecyclerAdapter(
            adapter,
            settings.defaultXRayDebugViewHolder,
            settings.minDebugViewSize,
            this::isInXRayMode
        )
    }

    override fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(adapter: RecyclerView.Adapter<*>): T {
        return (adapter as ScannableRecyclerAdapter<*>).decoratedAdapter as T
    }

    private fun updateAdapters() {
        adapters.forEach { weakAdapter ->
            weakAdapter.get()?.let { adapter ->
                adapter.notifyItemRangeChanged(0, adapter.itemCount,
                    XRayPayload
                )
            }
        }
    }
}