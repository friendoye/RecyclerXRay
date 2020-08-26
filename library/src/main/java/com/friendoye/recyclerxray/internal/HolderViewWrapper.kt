package com.friendoye.recyclerxray.internal

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.R
import com.friendoye.recyclerxray.XRayDebugViewHolder

internal class HolderViewWrapper(
    private val xRayDebugViewHolder: XRayDebugViewHolder,
    @Dimension(unit = Dimension.PX) private val minDebugViewSize: Int?
) {

    fun wrap(holderWrapperView: View,
             holderItemParams: ViewGroup.LayoutParams?,
             @RecyclerView.Orientation recyclerOrientation: Int): ConstraintLayout {
        val context = holderWrapperView.context
        val xRayContainer = ConstraintLayout(context).apply {
            id = R.id.parent_constraint_layout_id
            if (holderItemParams != null) {
                layoutParams = holderItemParams
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
        val debugLayoutConstraints = createMatchViewConstraint(
            debugLayout.id, holderWrapperView.id, recyclerOrientation
        )
        debugLayoutConstraints.applyTo(xRayContainer)

        val innerRecyclerIndicatorView = View(context).apply {
            id = R.id.inner_indicator_view_id
            visibility = View.GONE
            setBackgroundResource(R.drawable.bg_inner_recycler_indicator)
        }
        xRayContainer.addView(innerRecyclerIndicatorView)
        val indicatorViewConstraints = createMatchViewConstraint(
            innerRecyclerIndicatorView.id, holderWrapperView.id, recyclerOrientation
        )
        indicatorViewConstraints.applyTo(xRayContainer)

        return xRayContainer
    }

    private fun createMatchViewConstraint(childId: Int, parentId: Int,
                                          @RecyclerView.Orientation recyclerOrientation: Int) = ConstraintSet().apply {
        connect(childId, ConstraintSet.LEFT,   parentId, ConstraintSet.LEFT)
        connect(childId, ConstraintSet.RIGHT,  parentId, ConstraintSet.RIGHT)
        connect(childId, ConstraintSet.TOP,    parentId, ConstraintSet.TOP)
        connect(childId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
        minDebugViewSize?.let {
            when (recyclerOrientation) {
                RecyclerView.VERTICAL   -> constrainMinHeight(childId, it)
                RecyclerView.HORIZONTAL -> constrainMinWidth(childId, it)
            }
        }
    }
}