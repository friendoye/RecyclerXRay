package com.friendoye.recyclerxray.internal

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val failOnNotFullyWrappedAdapter: Boolean,
    private val scanner: Scanner = Scanner()
) : DelegateRecyclerAdapter<T>(decoratedAdapter),
    XRayCustomParamsAdapterProvider {

    private val holderViewWrapper = HolderViewWrapper(
        xRayDebugViewHolder,
        minDebugViewSize
    )

    private val innerAdapterWatcher = InnerAdapterWatcher<T>(
        xRayApi,
        nestedXRaySettingsProvider
    )

    private val overlayHideController = OverlayHideController(
        ownerAdapter = this,
        isInXRayModeProvider = isInXRayModeProvider
    )

    @RecyclerView.Orientation
    private var currentOrientation: Int = RecyclerView.VERTICAL

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        if (failOnNotFullyWrappedAdapter) {
            val recyclerAdapter = recyclerView.adapter
                ?: throw RuntimeException("Is it possible? If happened, please, report to library maintainer.")
            if (!recyclerAdapter.checkIsWrappedCorrectly()) {
                throw RecyclerAdapterNotFullyWrappedException()
            }
        }

        val layoutManager = recyclerView.layoutManager
        currentOrientation = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.orientation
            else -> RecyclerView.VERTICAL
        }
        overlayHideController.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        overlayHideController.onDetachedFromRecyclerView(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

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
            if (holder._xRayApiId == parentXRayApiId) {
                holder.bindXRayMode()
            }
        }

        if (enableNestedRecyclersSupport) {
            innerAdapterWatcher.startWatching(holder)
        }

        val xRayContainer = holderViewWrapper.wrap(
            holderWrapper, holderItemParams, currentOrientation
        )

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
        // ViewHolder might be reuse across several Adapters when used in ConcatAdapter.
        // To ensure correct bindings, rebind VH info before view bindings.
        holder.rebindInfo(xRayApiId = parentXRayApiId, xRayDebugLabel = debugXRayLabel)

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

        // ViewHolder might be reuse across several Adapters when used in ConcatAdapter.
        // To ensure correct bindings, rebind VH info before view bindings.
        holder.rebindInfo(xRayApiId = parentXRayApiId, xRayDebugLabel = debugXRayLabel)

        val shouldShowInnerAdapterXRay = enableNestedRecyclersSupport
                && innerAdapterWatcher.hasInnerAdapter(holder)

        holder.bindXRayMode(
            itemType = holder.itemViewType,
            isInXRayMode = isInXRayModeProvider(),
            showInnerAdapterIndicator = shouldShowInnerAdapterXRay,
            customParamsFromAdapter = provideCustomParams(holder.bindingAdapterPosition)
        )
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

                // WARNING: Temporary workaround. Remove it
                try {
                    view.setLoggableLinkClickListener(xRayResult, bindingAdapterPosition)
                } catch (e: Exception) {
                    // Do nothing...
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

    private fun View.setLoggableLinkClickListener(xRayResult: XRayResult, position: Int) {
        alpha = if (overlayHideController.isOverlayHidden[position]) 0.0f else 1.0f
        setOnClickListener {
            val loggableLinkToFile = xRayResult.viewHolderClass.getLoggableLinkToFileWithClass()
            Log.i(DEFAULT_LOG_TAG, loggableLinkToFile ?: "...")
            if (xRayResult.isViewVisibleForUser) {
                overlayHideController.toggleHidden(position)
                alpha = if (overlayHideController.isOverlayHidden[position]) 0.0f else 1.0f
            } else {
                xRayDebugViewHolder.onEmptyViewClick(this, xRayResult)
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