package com.friendoye.recyclerxray

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


class ScannableRecyclerAdapter<T : RecyclerView.ViewHolder>(
    decoratedAdapter: RecyclerView.Adapter<T>,
    private val xRayDebugViewHolder: XRayDebugViewHolder,
    private val scanner: Scanner = Scanner()
) : DelegateRecyclerAdapter<T>(decoratedAdapter), XRayCustomParamsAdapterProvider {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val xRayContainer = FrameLayout(parent.context)

        val holder = super.onCreateViewHolder(xRayContainer, viewType)
        xRayContainer.addView(holder.itemView)

        val debugLayout = xRayDebugViewHolder.provideView(xRayContainer)
        debugLayout.visibility = View.GONE
        debugLayout.isClickable = true
        debugLayout.tag = "DEBUG"
        xRayContainer.addView(debugLayout)

        val field = RecyclerView.ViewHolder::class.java.getDeclaredField("itemView")
        field.isAccessible = true
        field.set(holder, xRayContainer)

        return holder
    }

    override fun provideCustomParams(position: Int): Map<String, Any?>? {
        return if (decoratedAdapter is XRayCustomParamsAdapterProvider) {
            decoratedAdapter.provideCustomParams(position)
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bindXRayMode(
            itemType = getItemViewType(position),
            isInXRayMode = RecyclerXRay.isInXRayMode,
            customParamsFromAdapter = provideCustomParams(position)
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
            isInXRayMode = RecyclerXRay.isInXRayMode,
            customParamsFromAdapter = provideCustomParams(position)
        )
    }

    private fun T.bindXRayMode(itemType: Int, isInXRayMode: Boolean,
                               customParamsFromAdapter: Map<String, Any?>?) {
        (itemView as? ViewGroup)?.children?.forEach { view ->
            if (view.tag == "DEBUG") {
                view.isVisible = isInXRayMode
                val xRayResult = scanner.scan(this, itemType, customParamsFromAdapter)
                if (isInXRayMode) {
                    xRayDebugViewHolder.bindView(view, xRayResult)
                    view.setOnClickListener {
                        val loggableLinkToFile = xRayResult.viewHolderClass.getLoggableLinkToFileWithClass()
                        Log.i(DEFAULT_LOG_TAG, loggableLinkToFile ?: "...")
                    }
                }
            } else {
                view.isInvisible = isInXRayMode
            }
        }
    }
}