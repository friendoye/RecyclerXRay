package com.friendoye.recyclerxray.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayResult
import com.friendoye.recyclerxray.internal.DefaultXRayDebugViewHolder
import com.friendoye.recyclerxray.test.R


class RvIntegrationTestAdapter(
    val useDiffUtils: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<IntegrationTestItemType> = emptyList()
        set(value) {
            if (useDiffUtils) {
                val diffCallback = DiffCalculator(field, value)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                field = value
                diffResult.dispatchUpdatesTo(this)
            } else {
                field = value
                notifyDataSetChanged()
            }
        }

    fun changeItems(vararg items: IntegrationTestItemType) {
        this.items = items.toList()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is IntegrationTestItemType.Visible -> 1
            is IntegrationTestItemType.LargeVisible -> 2
            is IntegrationTestItemType.Ghost -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> VisibleViewHolder.fromParent(parent)
            2 -> LargeVisibleViewHolder.fromParent(parent)
            3 -> GhostViewHolder.fromParent(parent)
            else -> throw IllegalStateException("Could not find ItemType for viewType = $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is GhostViewHolder -> holder.bind(item as IntegrationTestItemType.Ghost)
        }
    }
}

sealed class IntegrationTestItemType {
    object Visible : IntegrationTestItemType()
    object LargeVisible : IntegrationTestItemType()
    data class Ghost(val forceVisibility: Boolean = false) : IntegrationTestItemType()
}

class VisibleViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {
    companion object {
        fun fromParent(parent: ViewGroup): VisibleViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_visible_test_layout, parent, false)
            return VisibleViewHolder(root)
        }
    }
}

class LargeVisibleViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {
    companion object {
        fun fromParent(parent: ViewGroup): LargeVisibleViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_visible_test_layout, parent, false)
            return LargeVisibleViewHolder(root)
        }
    }
}

class GhostViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {
    fun bind(item: IntegrationTestItemType.Ghost) = itemView.apply {
        layoutParams = layoutParams.apply {
            width = if (item.forceVisibility) context.dip(150) else 0
        }
    }

    companion object {
        fun fromParent(parent: ViewGroup): GhostViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_invisible_test_layout, parent, false)
            return GhostViewHolder(root)
        }
    }
}

internal class RvIntegrationXRayDebugViewHolder : DefaultXRayDebugViewHolder() {
    override fun provideView(parent: ViewGroup): View {
        return (super.provideView(parent) as ViewGroup).apply {
            getChildAt(0).apply {
                layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    setMargins(0)
                }
            }
        }
    }

    override fun prepareDebugText(result: XRayResult): String {
        return """
            ViewHolder class: ${result.viewHolderClass.simpleName}
        """.trimIndent()
    }
}