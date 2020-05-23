package com.friendoye.recyclerxray.xray

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.R


class ScannableRecyclerAdapter<T : RecyclerView.ViewHolder>(
    decoratedAdapter: RecyclerView.Adapter<T>
) : DelegateRecyclerAdapter<T>(decoratedAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val xRayContainer = FrameLayout(parent.context)

        val holder = super.onCreateViewHolder(xRayContainer, viewType)
        xRayContainer.addView(holder.itemView)

        val debugLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.xray_item_debug_layout, xRayContainer, false)
        debugLayout.visibility = View.GONE
        debugLayout.isClickable = true
        debugLayout.tag = "DEBUG"
        xRayContainer.addView(debugLayout)

        val field = RecyclerView.ViewHolder::class.java.getDeclaredField("itemView")
        field.isAccessible = true
        field.set(holder, xRayContainer)

        return holder
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindXRayMode(RecyclerXRay.isInXRayMode)
    }

    override fun onBindViewHolder(holder: T, position: Int, payloads: List<Any>) {
        val xRayPayload = payloads.asSequence()
            .filterIsInstance<XRayPayload>()
            .firstOrNull()
        val clearedPayload = payloads.asSequence()
            .filter { it !== xRayPayload }
            .toList()

        super.onBindViewHolder(holder, position, clearedPayload)

        holder.bindXRayMode(xRayPayload?.isInXRayMode ?: RecyclerXRay.isInXRayMode)
    }

    private fun T.bindXRayMode(isInXRayMode: Boolean) {
        (itemView as? ViewGroup)?.children?.forEach { view ->
            if (view.tag == "DEBUG") {
                view.isVisible = isInXRayMode
            } else {
                view.isInvisible = isInXRayMode
            }
        }
    }
}