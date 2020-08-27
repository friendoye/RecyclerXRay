package com.friendoye.recyclerxray.internal

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRaySettings
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicLong

internal class RealRecyclerXRayApi(
    defaultSettings: XRaySettings = XRaySettings.Builder().build()
) : RecyclerXRayApi {

    companion object {
        internal var counter = AtomicLong(0)
        fun createUniqueId(): Long = counter.getAndIncrement()
    }

    private val id: Long = createUniqueId()

    // Internal state
    // TODO: Check possibility not to keep several refs to same adapter
    internal val adapters: MutableSet<WeakReference<RecyclerView.Adapter<*>>> = mutableSetOf()
    internal var isInXRayMode = false
    override var settings: XRaySettings = defaultSettings

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

    override fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(adapter: T): RecyclerView.Adapter<VH> {
        return internalWrap(adapter, settings)
    }

    override fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> wrap(
        adapter: T,
        settings: XRaySettings
    ): RecyclerView.Adapter<VH> {
        return internalWrap(adapter, settings)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> unwrap(adapter: RecyclerView.Adapter<*>): T {
        return (adapter as ScannableRecyclerAdapter<*>).decoratedAdapter as T
    }

    private fun <T : RecyclerView.Adapter<VH>, VH : RecyclerView.ViewHolder> internalWrap(
        adapter: T,
        settings: XRaySettings
    ): RecyclerView.Adapter<VH> {
        if (adapter is ScannableRecyclerAdapter<*> && adapter.parentXRayApiId == id) {
            Log.i(
                DEFAULT_LOG_TAG,
                "Skipping wrapping same wrapped adapter for RecyclerXRay ${settings.label}($id)...)"
            )
            return adapter
        }

        adapters.add(adapter.asWeakRef())
        return ScannableRecyclerAdapter(
            adapter,
            this,
            id,
            settings.defaultXRayDebugViewHolder,
            settings.minDebugViewSize,
            settings.label,
            this::isInXRayMode,
            settings.enableNestedRecyclersSupport,
            settings.nestedXRaySettingsProvider,
            settings.failOnNotFullyWrappedAdapter
        )
    }

    private fun updateAdapters() {
        adapters.forEach { weakAdapter ->
            weakAdapter.get()?.let { adapter ->
                adapter.notifyItemRangeChanged(0, adapter.itemCount,
                    XRayPayload(id)
                )
            }
        }
    }
}
