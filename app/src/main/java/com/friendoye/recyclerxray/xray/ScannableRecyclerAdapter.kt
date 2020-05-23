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
import kotlinx.android.synthetic.main.xray_item_debug_layout.view.*


class ScannableRecyclerAdapter<T : RecyclerView.ViewHolder>(
    decoratedAdapter: RecyclerView.Adapter<T>,
    private val scanner: Scanner = Scanner()
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
        holder.bindXRayMode(
            itemType = getItemViewType(position),
            isInXRayMode = RecyclerXRay.isInXRayMode
        )
    }

    override fun onBindViewHolder(holder: T, position: Int, payloads: List<Any>) {
        val xRayPayload = payloads.asSequence()
            .filterIsInstance<XRayPayload>()
            .firstOrNull()
        val clearedPayload = payloads.asSequence()
            .filter { it !== xRayPayload }
            .toList()

        super.onBindViewHolder(holder, position, clearedPayload)

        holder.bindXRayMode(
            itemType = getItemViewType(position),
            isInXRayMode = xRayPayload?.isInXRayMode ?: RecyclerXRay.isInXRayMode
        )
    }

    private fun T.bindXRayMode(itemType: Int, isInXRayMode: Boolean) {
        (itemView as? ViewGroup)?.children?.forEach { view ->
            if (view.tag == "DEBUG") {
                view.isVisible = isInXRayMode
                val xRayResult = scanner.scan(this, itemType)
                if (isInXRayMode) {
                    view.bindXRayResult(xRayResult)
                }
            } else {
                view.isInvisible = isInXRayMode
            }
        }
    }

    private fun View.bindXRayResult(result: XRayResult) {
        assert(tag == "DEBUG")

        debug_info_text_view.apply {
            text = """
                ViewHolder class:
                ${result.viewHolderClass.name}
                ViewHolder type: ${result.viewHolderType}
            """.trimIndent()
            setBackgroundColor(result.color)
        }
    }
}