package com.friendoye.recyclerxray.xray

import androidx.recyclerview.widget.RecyclerView


@Suppress("SimplifyBooleanWithConstants")
object RecyclerXRay {

    private val adapters: MutableSet<RecyclerView.Adapter<*>> = mutableSetOf()
    internal var isInXRayMode = false

    fun toggleSecrets() {
        if (isInXRayMode) {
            hideSecrets()
        } else {
            showSecrets()
        }
    }

    fun showSecrets() {
        assert(isInXRayMode == false)
        isInXRayMode = true
        updateAdapters()
    }

    fun hideSecrets() {
        assert(isInXRayMode == true)
        isInXRayMode = false
        updateAdapters()
    }

    fun <T : RecyclerView.Adapter<VH>, VH: RecyclerView.ViewHolder> wrap(adapter: T): ScannableRecyclerAdapter<VH> {
        adapters.add(adapter)
        return ScannableRecyclerAdapter(adapter)
    }

    private fun updateAdapters() {
        adapters.forEach { adapter ->
            adapter.notifyItemRangeChanged(0, adapter.itemCount, XRayPayload)
        }
    }
}