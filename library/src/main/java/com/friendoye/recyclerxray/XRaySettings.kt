package com.friendoye.recyclerxray

import androidx.annotation.Dimension
import androidx.annotation.Dimension.PX


data class XRaySettings internal constructor(
    val defaultXRayDebugViewHolder: XRayDebugViewHolder,
    @Dimension(unit = PX) val minDebugViewSize: Int?,
    val label: String?
) {

    class Builder(
        internal var debugViewHolder: XRayDebugViewHolder = DefaultXRayDebugViewHolder(),
        @Dimension(unit = PX) internal var minDebugViewSize: Int? = null,
        internal var label: String? = null
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

        fun build(): XRaySettings = XRaySettings(
            defaultXRayDebugViewHolder = debugViewHolder,
            minDebugViewSize = minDebugViewSize,
            label = label
        )
    }
}