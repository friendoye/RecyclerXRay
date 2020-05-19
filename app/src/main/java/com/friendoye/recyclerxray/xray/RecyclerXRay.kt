package com.friendoye.recyclerxray.xray

import androidx.recyclerview.widget.RecyclerView


@Suppress("SimplifyBooleanWithConstants")
class RecyclerXRay private constructor(
    private val recycler: RecyclerView,
    private val adapter: RecyclerView.Adapter<*>
) {

    private var inXRayMode = false

    fun showSecrets() {
        assert(inXRayMode == false)

        // 1) try to replace adapter - hold on
        inXRayMode = true
        // 2) custom vh

        //get all viewholders
        //hide real content
        //show info views
        //refresh
    }

    fun hideSecrets() {
        assert(inXRayMode == true)
    }

    companion object {
        fun from(recycler: RecyclerView,
                 adapter: RecyclerView.Adapter<*>) = RecyclerXRay(recycler, adapter)
    }
}