package com.friendoye.recyclerxray.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayResult
import com.friendoye.recyclerxray.DefaultXRayDebugViewHolder
import com.friendoye.recyclerxray.test.R



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
            is IntegrationTestItemType.SmallOne -> 3
            is IntegrationTestItemType.SmallTwo -> 4
            is IntegrationTestItemType.Ghost -> 5
            is IntegrationTestItemType.InnerRecycler -> 6
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
                .inflate(R.layout.item_integration_large_visible_test_layout, parent, false)
            return LargeVisibleViewHolder(root)
        }
    }
}

class SmallOneViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {
    companion object {
        fun fromParent(parent: ViewGroup): SmallOneViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_small_one_test_layout, parent, false)
            return SmallOneViewHolder(root)
        }
    }
}

class SmallTwoViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {
    companion object {
        fun fromParent(parent: ViewGroup): SmallTwoViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_small_two_test_layout, parent, false)
            return SmallTwoViewHolder(root)
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

class InnerRecyclerViewHolder private constructor(
    root: View,
    private val innerWrapper: (RecyclerView.Adapter<*>) -> RecyclerView.Adapter<*>
) : RecyclerView.ViewHolder(root) {
    private val recycler: RecyclerView
        get() = (itemView as ViewGroup).getChildAt(0) as RecyclerView

    private var currentAdapter = createInnerTestAdapter()

    init {
        recycler.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = innerWrapper(currentAdapter)
        }
    }

    fun bind(item: IntegrationTestItemType.InnerRecycler) = itemView.apply {
        val recycler = (itemView as ViewGroup).getChildAt(0) as RecyclerView
        if (item.changeAdapter) {
            // 1) Replace adapter on each re-bind
            currentAdapter = createInnerTestAdapter(*item.items.toTypedArray(), useDiffUtils = item.useDiffUtils)
            recycler.adapter = innerWrapper(currentAdapter)
            recycler.post { recycler.requestLayout() }
        } else {
            // 2) Replace adapter items on each re-bind
            currentAdapter.apply {
                useDiffUtils = item.useDiffUtils
                items = item.items
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            innerWrapper: (RecyclerView.Adapter<*>) -> RecyclerView.Adapter<*>
        ): InnerRecyclerViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_inner_adapter_test_layout, parent, false)
            return InnerRecyclerViewHolder(root, innerWrapper)
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