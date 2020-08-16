package com.friendoye.recyclerxray.internal

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.*
import com.friendoye.recyclerxray.testing.ExceptionShooter


internal class ScannableRecyclerAdapter<T : RecyclerView.ViewHolder>(
    decoratedAdapter: RecyclerView.Adapter<T>,
    xRayApi: RealRecyclerXRayApi, // TODO: Should be retrievable by id?
    val parentXRayApiId: Long,
    private val xRayDebugViewHolder: XRayDebugViewHolder,
    @Dimension(unit = Dimension.PX) private val minDebugViewSize: Int?,
    private val debugXRayLabel: String?,
    private val isInXRayModeProvider: () -> Boolean,
    private val enableNestedRecyclersSupport: Boolean,
    private val nestedXRaySettingsProvider: NestedXRaySettingsProvider?,
    private val scanner: Scanner = Scanner()
) : DelegateRecyclerAdapter<T>(decoratedAdapter),
    XRayCustomParamsAdapterProvider {

    private val innerAdapterWatcher = InnerAdapterWatcher<T>(
        xRayApi,
        nestedXRaySettingsProvider
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val holderWrapper = FrameLayout(parent.context).apply {
            id = R.id.holder_item_view_placeholder_id
        }
        val holder = super.onCreateViewHolder(holderWrapper, viewType)
        if (holder.isWrappedByXRay()) {
            ExceptionShooter.fire(
                MultipleRecyclerXRayAttachedException("""
                    |It seems like ViewHolder was decorated by several ${ScannableRecyclerAdapter::class.java.simpleName}'s.
                    |${holder.prepareDebugInfo()}
                """.trimMargin())
            )
        }

        val holderItemParams = holder.itemView.layoutParams
        holderWrapper.addView(holder.itemView)

        // TODO: Unstable API
        holderWrapper.doOnLayout {
            holder.bindXRayMode()
        }

        if (enableNestedRecyclersSupport) {
            innerAdapterWatcher.startWatching(holder)
        }

        val xRayContainer = wrap(holderWrapper, holderItemParams)

        return holder.replaceItemView(
            xRayWrapperContainer = xRayContainer,
            xRayApiId = parentXRayApiId,
            xRayDebugLabel = debugXRayLabel
        )
    }

    override fun provideCustomParams(position: Int): Map<String, Any?>? {
        return if (decoratedAdapter is XRayCustomParamsAdapterProvider) {
            decoratedAdapter.provideCustomParams(position)
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        val shouldShowInnerAdapterXRay = enableNestedRecyclersSupport
                && innerAdapterWatcher.hasInnerAdapter(holder)

        holder.originalItemViewContext { holder ->
            super.onBindViewHolder(holder, position)
            holder.bindXRayMode(
                itemType = holder.itemViewType,
                isInXRayMode = isInXRayModeProvider(),
                showInnerAdapterIndicator = shouldShowInnerAdapterXRay,
                customParamsFromAdapter = provideCustomParams(holder.bindingAdapterPosition)
            )
        }
    }

    override fun onBindViewHolder(holder: T, position: Int, payloads: List<Any>) {
        holder.originalItemViewContext { holder ->
            val xRayPayload = payloads.asSequence()
                .filterIsInstance<XRayPayload>()
                .filter { it.xRayApiId == parentXRayApiId }
                .firstOrNull()
            val clearedPayload = payloads.asSequence()
                .filter { it !== xRayPayload }
                .toList()

            super.onBindViewHolder(holder, position, clearedPayload)
        }

        val shouldShowInnerAdapterXRay = enableNestedRecyclersSupport
                && innerAdapterWatcher.hasInnerAdapter(holder)

        holder.bindXRayMode(
            itemType = holder.itemViewType,
            isInXRayMode = isInXRayModeProvider(),
            showInnerAdapterIndicator = shouldShowInnerAdapterXRay,
            customParamsFromAdapter = provideCustomParams(holder.bindingAdapterPosition)
        )
    }

    private fun wrap(holderWrapperView: View, holderItemParams: ViewGroup.LayoutParams?): ConstraintLayout {
        val context = holderWrapperView.context
        val xRayContainer = ConstraintLayout(context).apply {
            id = R.id.parent_constraint_layout_id
            if (holderItemParams != null) {
                layoutParams = holderItemParams
            }
        }

        fun createMatchViewConstraint(childId: Int, parentId: Int) = ConstraintSet().apply {
            connect(childId, ConstraintSet.LEFT,   parentId, ConstraintSet.LEFT)
            connect(childId, ConstraintSet.RIGHT,  parentId, ConstraintSet.RIGHT)
            connect(childId, ConstraintSet.TOP,    parentId, ConstraintSet.TOP)
            connect(childId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
            minDebugViewSize?.let {
                constrainMinHeight(childId, it)
            }
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
        val debugLayoutConstraints = createMatchViewConstraint(debugLayout.id, holderWrapperView.id)
        debugLayoutConstraints.applyTo(xRayContainer)

        val innerRecyclerIndicatorView = View(context).apply {
            id = R.id.inner_indicator_view_id
            visibility = View.GONE
            setBackgroundResource(R.drawable.bg_inner_recycler_indicator)
        }
        xRayContainer.addView(innerRecyclerIndicatorView)
        val indicatorViewConstraints = createMatchViewConstraint(innerRecyclerIndicatorView.id, holderWrapperView.id)
        indicatorViewConstraints.applyTo(xRayContainer)

        return xRayContainer
    }

    // TODO: Unstable API
    private fun T.bindXRayMode() {
        val shouldShowInnerAdapterXRay = enableNestedRecyclersSupport
                && innerAdapterWatcher.hasInnerAdapter(this)

        bindXRayMode(
            // WARNING: Temporary workaround. Remove it
            itemType = itemViewType, //getItemViewType(bindingAdapterPosition),
            isInXRayMode = isInXRayModeProvider(),
            showInnerAdapterIndicator = shouldShowInnerAdapterXRay,
            // WARNING: Temporary workaround. Remove it
            customParamsFromAdapter = try {
                provideCustomParams(bindingAdapterPosition)
            } catch (e: Throwable) {
                null
            }
        )
    }

    private fun T.bindXRayMode(itemType: Int, isInXRayMode: Boolean,
                               showInnerAdapterIndicator: Boolean,
                               customParamsFromAdapter: Map<String, Any?>?) {
        (itemView as? ViewGroup)?.children?.forEach { view ->
            if (view.tag == "DEBUG") {
                view.isVisible = isInXRayMode && !showInnerAdapterIndicator
                val xRayResult = scanner.scan(
                    this, itemType, customParamsFromAdapter, originalItemView
                )
                if (isInXRayMode) {
                    view.isClickable = true
                    xRayDebugViewHolder.bindView(view, xRayResult)
                }
            } else if (view.id == R.id.inner_indicator_view_id) {
                view.isVisible = isInXRayMode && showInnerAdapterIndicator
            } else {
                // Do not hide ViewHolder view,
                // so we could preview it on item click
                // view.isInvisible = isInXRayMode
            }
        }
    }

    private fun <T : RecyclerView.ViewHolder> T.prepareDebugInfo(): String {
        return """
            |Debug info:
            |> RecyclerXRays: $debugXRayLabel($parentXRayApiId) -> $_xRayDebugLabel($_xRayApiId)
            |> Decorated ViewHolder class: ${javaClass.simpleName}
        """.trimMargin()
    }
}