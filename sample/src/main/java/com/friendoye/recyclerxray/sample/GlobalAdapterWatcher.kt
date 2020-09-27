package com.friendoye.recyclerxray.sample

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.RecyclerXRay
import java.util.*

internal class GlobalAdapterWatcher {

    private val rvToAdapterMap = WeakHashMap<RecyclerView, RecyclerView.Adapter<*>>()

    companion object {
        @JvmField
        val TAG = GlobalAdapterWatcher::class.java.simpleName
    }

    fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?,
                         newAdapter: RecyclerView.Adapter<*>?) {
        rvToAdapterMap.entries.forEach { (weakRefRv, adapter) ->
            if (oldAdapter == adapter) {
                processRecycler(weakRefRv, newAdapter)
            }
        }
    }

    fun startWatching(originalItemView: ViewGroup) {
        // TODO: Investigate, when to remove this listener...
        originalItemView.viewTreeObserver.addOnGlobalLayoutListener {
            // Suppose we will only have 1 inner RecyclerView inside ViewHolder,
            // because having several inner recyclers seems improbable...

            val recyclers = originalItemView.findAllViews<RecyclerView>()
            recyclers.forEach { recycler ->
                processRecycler(recycler, recycler?.adapter)
            }
        }
    }

    private fun processRecycler(recycler: RecyclerView, newAdapter: RecyclerView.Adapter<*>?) {
        val wrappedInnerAdapter = tryWrapNestedAdapter(newAdapter)
        if (recycler?.adapter != wrappedInnerAdapter) {
            recycler?.adapter = wrappedInnerAdapter
        }

        if (rvToAdapterMap[recycler] != recycler?.adapter) {
            if (rvToAdapterMap[recycler] == null) {
                Log.d(
                    "TEST",
                    "[$TAG] New adapter: ${recycler?.adapter}"
                )
            } else {
                Log.d(
                    "TEST",
                    "[$TAG] ${rvToAdapterMap[recycler]} -> ${recycler?.adapter}"
                )
            }
            rvToAdapterMap[recycler] = recycler?.adapter
        }
    }

    private fun <VH : RecyclerView.ViewHolder> tryWrapNestedAdapter(
        innerAdapter: RecyclerView.Adapter<VH>?
    ): RecyclerView.Adapter<*>? {
        var adapter = innerAdapter
        if (adapter != null && !adapter.javaClass.simpleName.contains("ScannableRecyclerAdapter")) {
//            val nestedXRaySettings = RecyclerXRay.settings // TODO: Refactor
//                .nestedXRaySettingsProvider
//                ?.provide(adapter)
//            if (nestedXRaySettings != null) {
                adapter = RecyclerXRay.wrap(adapter)
//            }
        }
        return adapter
    }
}


inline fun <reified T> View.findAllViews(): Sequence<T> {
    return sequence { findAllViews(this@findAllViews, T::class.java) }
}

suspend fun <T> SequenceScope<T>.findAllViews(root: View, clazz: Class<T>) {
    if (clazz.isInstance(root)) {
        @Suppress("UNCHECKED_CAST")
        yield(root as T)
    } else if (root is ViewGroup) {
        root.children.forEach { findAllViews(it, clazz) }
    }
}