package com.friendoye.recyclerxray

import androidx.annotation.Dimension
import androidx.annotation.Dimension.PX
import androidx.recyclerview.widget.RecyclerView

/**
 * Class, that stores all different settings for specific [LocalRecyclerXRay].
 */
data class XRaySettings internal constructor(
    val defaultXRayDebugViewHolder: XRayDebugViewHolder,
    @Dimension(unit = PX) val minDebugViewSize: Int?,
    val label: String?,
    val enableNestedRecyclersSupport: Boolean,
    val nestedXRaySettingsProvider: NestedXRaySettingsProvider?
) {

    class Builder(
        internal var debugViewHolder: XRayDebugViewHolder = DefaultXRayDebugViewHolder(),
        @Dimension(unit = PX) internal var minDebugViewSize: Int? = null,
        internal var label: String? = null,
        internal var enableNestedRecyclersSupport: Boolean = false,
        internal var nestedXRaySettingsProvider: NestedXRaySettingsProvider? = null
    ) {

        /**
         * Specifies [XRayDebugViewHolder], that will be used for creating and binding
         * every debugView during inspection.
         */
        fun withDefaultXRayDebugViewHolder(debugViewHolder: XRayDebugViewHolder) = apply {
            this.debugViewHolder = debugViewHolder
        }

        /**
         * Specifies minimal size of width or height, if some of them will equal to 0.
         */
        fun withMinDebugViewSize(@Dimension(unit = PX) size: Int) = apply {
            minDebugViewSize = size
        }

        /**
         * Associated string label, that will be used for logs and debug information
         * in [LocalRecyclerXRay].
         */
        fun withLabel(label: String) = apply {
            this.label = label
        }

        /**
         * Enables or disables inspection support for nested [RecyclerView.Adapter].
         * Nested [RecyclerView.Adapter]'s could be found, if some [RecyclerView.ViewHolder]'s
         * `itemView` has [RecyclerView] inside.
         */
        fun enableNestedRecyclersSupport(isEnabled: Boolean) = apply {
            enableNestedRecyclersSupport = isEnabled
        }

        /**
         * [XRaySettings] provider for inner [LocalRecyclerXRay]'s.
         * Used only if `enableNestedRecyclersSupport == true`.
         */
        fun withNestedXRaySettingsProvider(settingsProvider: NestedXRaySettingsProvider?) = apply {
            nestedXRaySettingsProvider = settingsProvider
        }

        /**
         * Prepares [XRaySettings].
         */
        fun build(): XRaySettings = XRaySettings(
            defaultXRayDebugViewHolder = debugViewHolder,
            minDebugViewSize = minDebugViewSize,
            label = label,
            enableNestedRecyclersSupport = enableNestedRecyclersSupport,
            nestedXRaySettingsProvider = nestedXRaySettingsProvider
        )
    }
}