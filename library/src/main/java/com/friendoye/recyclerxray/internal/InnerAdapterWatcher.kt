package com.friendoye.recyclerxray.internal

import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.NestedXRaySettingsProvider
import com.friendoye.recyclerxray.testing.InternalLog

internal class InnerAdapterWatcher<T : RecyclerView.ViewHolder>(
    private val xRayApi: RealRecyclerXRayApi,
    private val nestedXRaySettingsProvider: NestedXRaySettingsProvider?,
) {

    companion object {
        @JvmField
        val TAG = InnerAdapterWatcher::class.java.simpleName
    }

    fun hasInnerAdapter(holder: T) = holder.innerRecycler?.adapter != null

    fun startWatching(holder: T) {
        // TODO: Investigate, when to remove this listener...
        holder.originalItemView.viewTreeObserver.addOnGlobalLayoutListener {
            // Suppose we will only have 1 inner RecyclerView inside ViewHolder,
            // because having several inner recyclers seems improbable...
            val recycler = holder.innerRecycler
            val wrappedInnerAdapter = tryWrapNestedAdapter(recycler?.adapter)
            if (recycler?.adapter != wrappedInnerAdapter) {
                recycler?.adapter = wrappedInnerAdapter
            }

            if (holder.savedInnerAdapter != recycler?.adapter) {
                if (holder.savedInnerAdapter == null) {
                    InternalLog.d(
                        DEFAULT_INTERNAL_LOG_TAG,
                        "[$TAG] New adapter: ${recycler?.adapter}",
                    )
                } else {
                    InternalLog.d(
                        DEFAULT_INTERNAL_LOG_TAG,
                        "[$TAG] ${holder.savedInnerAdapter} -> ${recycler?.adapter}",
                    )
                }
                holder.savedInnerAdapter = recycler?.adapter
            }
        }
    }

    private fun <VH : RecyclerView.ViewHolder> tryWrapNestedAdapter(
        innerAdapter: RecyclerView.Adapter<VH>?,
    ): RecyclerView.Adapter<*>? {
        var adapter = innerAdapter
        if (adapter != null && adapter !is ScannableRecyclerAdapter<*>) {
            val nestedXRaySettings = nestedXRaySettingsProvider
                ?.provide(adapter)
            if (nestedXRaySettings != null) {
                adapter = xRayApi.wrap(adapter, nestedXRaySettings)
            }
        }
        return adapter
    }
}
