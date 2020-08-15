package com.friendoye.recyclerxray

import androidx.annotation.Dimension
import androidx.annotation.Dimension.PX
import androidx.recyclerview.widget.RecyclerView

interface NestedRecyclerXRayProvider {
    fun provide(nestedAdapter: RecyclerView.Adapter<*>, isDecorated: Boolean): XRaySettings?
}

data class XRaySettings internal constructor(
    val defaultXRayDebugViewHolder: XRayDebugViewHolder,
    @Dimension(unit = PX) val minDebugViewSize: Int?,
    val label: String?,
    val enableNestedRecyclersSupport: Boolean,
    val recyclerXRayProvider: NestedRecyclerXRayProvider?
) {

    class Builder(
        internal var debugViewHolder: XRayDebugViewHolder = DefaultXRayDebugViewHolder(),
        @Dimension(unit = PX) internal var minDebugViewSize: Int? = null,
        internal var label: String? = null,
        internal var enableNestedRecyclersSupport: Boolean = false,
        internal var recyclerXRayProvider: NestedRecyclerXRayProvider? = null
    ) {
        fun withDefaultXRayDebugViewHolder(debugViewHolder: XRayDebugViewHolder) = apply {
            this.debugViewHolder = debugViewHolder
        }

        fun withMinDebugViewSize(@Dimension(unit = PX) size: Int) = apply {
            minDebugViewSize = size
        }

        fun withLabel(label: String) = apply {
            this.label = label
        }

        fun enableNestedRecyclersSupport(isEnabled: Boolean) = apply {
            enableNestedRecyclersSupport = isEnabled
        }

        fun withRecyclerXRayProvider(provider: NestedRecyclerXRayProvider) = apply {
            recyclerXRayProvider = provider
        }

        fun build(): XRaySettings = XRaySettings(
            defaultXRayDebugViewHolder = debugViewHolder,
            minDebugViewSize = minDebugViewSize,
            label = label,
            enableNestedRecyclersSupport = enableNestedRecyclersSupport,
            recyclerXRayProvider = recyclerXRayProvider
        )
    }
}