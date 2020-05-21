package com.friendoye.recyclerxray.xray

import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Proxy


@Suppress("SimplifyBooleanWithConstants")
object RecyclerXRay {

    private val adapters: MutableSet<RecyclerView.Adapter<*>> = mutableSetOf()
    private var inXRayMode = false

    fun toggleSecrets() {
        if (inXRayMode) {
            hideSecrets()
        } else {
            showSecrets()
        }
    }

    fun showSecrets() {
        assert(inXRayMode == false)

        inXRayMode = true
        updateAdapters()

        // 1) try to replace adapter - hold on
        // 2) custom vh
        //get all viewholders
        //hide real content
        //show info views
        //refresh
    }

    fun hideSecrets() {
        assert(inXRayMode == true)

        inXRayMode = false
        updateAdapters()
    }

    private fun updateAdapters() {
        val payload = XRayPayload(inXRayMode)
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