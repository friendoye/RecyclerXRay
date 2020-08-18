package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView

/**
 * Interface, that is used for supporting nested [RecyclerView] inspection.
 */
interface NestedXRaySettingsProvider {

    /**
     * Provides [XRaySettings] for found nested [RecyclerView]'s adapter.
     * If returns `null`, inspection will not be done for given adapter.
     */
    fun provide(nestedAdapter: RecyclerView.Adapter<*>): XRaySettings?
}