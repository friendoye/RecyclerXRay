package com.friendoye.recyclerxray.internal

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.NestedRecyclerXRayProvider

internal class InnerAdapterWatcher<T : RecyclerView.ViewHolder>(
    private val xRayApi: RealRecyclerXRayApi,
    private val parentXRayApiId: Long,
    private val nestedRecyclerXRayProvider: NestedRecyclerXRayProvider?
) {

    fun hasInnerAdapter(holder: T) = holder.innerAdapter != null

    fun startWatching(holder: T) {
        // TODO: Investigate, when to remove this listener...
        holder.originalItemView.viewTreeObserver.addOnGlobalLayoutListener {
            val recycler = holder.innerRecycler
//            Log.i(DEFAULT_LOG_TAG, "${holder.javaClass.simpleName} -> $recycler")
//            if (holder.innerRecycler != recycler) {
                val wrappedInnerAdapter = tryWrapNestedAdapter(recycler?.adapter)
                if (recycler?.adapter != wrappedInnerAdapter) {
                    recycler?.adapter = wrappedInnerAdapter
                }

                Log.i(DEFAULT_LOG_TAG, "${holder.innerAdapter} -> ${recycler?.adapter}")
                recycler?.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    if (holder.innerAdapter != recycler.adapter) {
                        Log.i(DEFAULT_LOG_TAG, "${holder.innerAdapter} -> ${recycler.adapter}")
                        val wrappedInnerAdapter = tryWrapNestedAdapter(recycler.adapter)
                        if (recycler.adapter != wrappedInnerAdapter) {
                            recycler.adapter = wrappedInnerAdapter
                        }
                    }
                }
//            }
        }
    }

    private fun <VH : RecyclerView.ViewHolder> tryWrapNestedAdapter(
        innerAdapter: RecyclerView.Adapter<VH>?
    ): RecyclerView.Adapter<*>? {
        var adapter = innerAdapter
        if (adapter != null && adapter !is ScannableRecyclerAdapter<*>) {
            val nestedXRaySettings = nestedRecyclerXRayProvider
                ?.provide(adapter, false)
            if (nestedXRaySettings != null) {
                adapter = xRayApi.wrap(adapter, nestedXRaySettings)
            }
        }
        return adapter
    }
}