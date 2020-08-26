package com.friendoye.recyclerxray.internal

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.NestedXRaySettingsProvider

internal class InnerAdapterWatcher<T : RecyclerView.ViewHolder>(
    private val xRayApi: RealRecyclerXRayApi,
    private val nestedXRaySettingsProvider: NestedXRaySettingsProvider?
) {

    fun hasInnerAdapter(holder: T) = holder.innerAdapter != null

    fun startWatching(holder: T) {
        // TODO: Investigate, when to remove this listener...
        holder.originalItemView.viewTreeObserver.addOnGlobalLayoutListener {
            val recycler = holder.innerRecycler
            val wrappedInnerAdapter = tryWrapNestedAdapter(recycler?.adapter)
            if (recycler?.adapter != wrappedInnerAdapter) {
                recycler?.adapter = wrappedInnerAdapter
            }

            if (holder.innerAdapter != null) {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "New adapter: ${holder.innerAdapter}")
            }
            recycler?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                if (holder.innerAdapter != recycler.adapter) {
                    Log.d(DEFAULT_INTERNAL_LOG_TAG, "${holder.innerAdapter} -> ${recycler.adapter}")
                    val wrappedInnerAdapter = tryWrapNestedAdapter(recycler.adapter)
                    if (recycler.adapter != wrappedInnerAdapter) {
                        recycler.adapter = wrappedInnerAdapter
                    }
                }
            }
        }
    }

    private fun <VH : RecyclerView.ViewHolder> tryWrapNestedAdapter(
        innerAdapter: RecyclerView.Adapter<VH>?
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
