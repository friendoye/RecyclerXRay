package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference


@Suppress("SimplifyBooleanWithConstants")
object RecyclerXRay {

    private val adapters: MutableSet<WeakReference<RecyclerView.Adapter<*>>> = mutableSetOf()
    internal var isInXRayMode = false
    var settings: XRaySettings =
        DEFAULT_X_RAY_SETTINGS

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
        adapters.add(adapter.asWeakRef())
        return ScannableRecyclerAdapter(
            adapter,
            settings.defaultXRayDebugViewHolder
        )
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