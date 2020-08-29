package com.friendoye.recyclerxray.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.DefaultXRayDebugViewHolder
import com.friendoye.recyclerxray.XRayResult

internal fun createTestAdapter(
    vararg items: IntegrationTestItemType,
    useDiffUtils: Boolean = false,
    innerWrapper: (RecyclerView.Adapter<*>) -> RecyclerView.Adapter<*> = { it }
): RvIntegrationTestAdapter {
    return RvIntegrationTestAdapter(useDiffUtils, innerWrapper).apply {
        this.items = items.toList()
    }
}

internal fun createTestAdapterWithDiffUtil(vararg items: IntegrationTestItemType): RvIntegrationTestAdapter {
    return createTestAdapter(*items.toList().toTypedArray(), useDiffUtils = true)
}

class RvIntegrationTestAdapter(
    val useDiffUtils: Boolean = false,
    val innerWrapper: (RecyclerView.Adapter<*>) -> RecyclerView.Adapter<*> = { it }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<IntegrationTestItemType> = emptyList()
        set(value) {
            if (useDiffUtils) {
                val diffCallback = object : DiffCalculator<IntegrationTestItemType>(field, value) {
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        val oldItem = getOldItem(oldItemPosition)
                        val newItem = getNewItem(newItemPosition)

                        // To proper change internal structure of InnerRecycler,
                        // we treat all InnerRecycler items as the same.
                        if (oldItem is IntegrationTestItemType.InnerRecycler &&
                            newItem is IntegrationTestItemType.InnerRecycler) {
                            return true
                        } else {
                            return super.areItemsTheSame(oldItemPosition, newItemPosition)
                        }
                    }
                }
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
            is IntegrationTestItemType.SmallOne -> 3
            is IntegrationTestItemType.SmallTwo -> 4
            is IntegrationTestItemType.Ghost -> 5
            is IntegrationTestItemType.InnerRecycler -> 6
            is IntegrationTestItemType.Indexed -> 7
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> VisibleViewHolder.fromParent(parent)
            2 -> LargeVisibleViewHolder.fromParent(parent)
            3 -> SmallOneViewHolder.fromParent(parent)
            4 -> SmallTwoViewHolder.fromParent(parent)
            5 -> GhostViewHolder.fromParent(parent)
            6 -> InnerRecyclerViewHolder.create(parent, innerWrapper)
            7 -> SmallTwoViewHolder.fromParent(parent)
            else -> throw IllegalStateException("Could not find ItemType for viewType = $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is GhostViewHolder -> holder.bind(item as IntegrationTestItemType.Ghost)
            is InnerRecyclerViewHolder -> holder.bind(item as IntegrationTestItemType.InnerRecycler)
        }
    }
}

sealed class IntegrationTestItemType {
    object Visible : IntegrationTestItemType()
    object LargeVisible : IntegrationTestItemType()
    object SmallOne : IntegrationTestItemType()
    object SmallTwo : IntegrationTestItemType()
    data class Ghost(val forceVisibility: Boolean = false) : IntegrationTestItemType()
    data class InnerRecycler(
        val changeAdapter: Boolean,
        val useDiffUtils: Boolean,
        val items: List<InnerTestItemType>
    ) : IntegrationTestItemType()
    data class Indexed(val index: Int) : IntegrationTestItemType()
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
