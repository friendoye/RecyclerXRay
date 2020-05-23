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

        // 1) try to replace adapter - hold on
        // 2) custom vh
        //get all viewholders
        //hide real content
        //show info views
        //refresh
    }

    fun hideSecrets() {
        assert(isInXRayMode == true)

        isInXRayMode = false
        updateAdapters()
    }

    private fun updateAdapters() {
        val payload = XRayPayload(isInXRayMode)
        adapters.forEach { adapter ->
            adapter.notifyItemRangeChanged(0, adapter.itemCount, payload)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : RecyclerView.Adapter<VH>, VH: RecyclerView.ViewHolder> wrap(adapter: T): ScannableRecyclerAdapter<VH> {
        adapters.add(adapter)
        return ScannableRecyclerAdapter(adapter)
    }
}

data class XRayPayload(val isInXRayMode: Boolean)