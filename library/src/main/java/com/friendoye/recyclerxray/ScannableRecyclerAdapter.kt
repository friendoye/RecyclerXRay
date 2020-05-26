package com.friendoye.recyclerxray

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


class ScannableRecyclerAdapter<T : RecyclerView.ViewHolder>(
    decoratedAdapter: RecyclerView.Adapter<T>,
    private val xRayDebugViewHolder: XRayDebugViewHolder,
    @Dimension(unit = Dimension.PX) private val minDebugViewSize: Int?,
    private val scanner: Scanner = Scanner()
) : DelegateRecyclerAdapter<T>(decoratedAdapter), XRayCustomParamsAdapterProvider {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val holderWrapper = FrameLayout(parent.context).apply {
            id = R.id.holder_item_view_placeholder_id
        }
        val holder = super.onCreateViewHolder(holderWrapper, viewType)
        holderWrapper.addView(holder.itemView)

        val xRayContainer = wrap(holderWrapper)

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

    private fun wrap(holderWrapperView: View): ConstraintLayout {
        val context = holderWrapperView.context
        val xRayContainer = ConstraintLayout(context).apply {
            id = R.id.parent_constraint_layout_id
        }

        xRayContainer.addView(holderWrapperView,
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        val debugLayout = xRayDebugViewHolder.provideView(xRayContainer).apply {
            id = R.id.debug_layout_id
            visibility = View.GONE
            isClickable = true
            tag = "DEBUG"
        }
        xRayContainer.addView(debugLayout)

        val debugLayoutConstraints = ConstraintSet().apply {
            connect(debugLayout.id, ConstraintSet.LEFT,   holderWrapperView.id, ConstraintSet.LEFT)
            connect(debugLayout.id, ConstraintSet.RIGHT,  holderWrapperView.id, ConstraintSet.RIGHT)
            connect(debugLayout.id, ConstraintSet.TOP,    holderWrapperView.id, ConstraintSet.TOP)
            connect(debugLayout.id, ConstraintSet.BOTTOM, holderWrapperView.id, ConstraintSet.BOTTOM)
            minDebugViewSize?.let {
                constrainMinHeight(debugLayout.id, it)
            }
        }
        debugLayoutConstraints.applyTo(xRayContainer)

        return xRayContainer
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