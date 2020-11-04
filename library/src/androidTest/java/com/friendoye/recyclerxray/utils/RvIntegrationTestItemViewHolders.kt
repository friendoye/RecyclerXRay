package com.friendoye.recyclerxray.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.test.R

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
            height = if (item.forceVisibility) context.dip(150) else ViewGroup.LayoutParams.WRAP_CONTENT
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

    init {
        recycler.itemAnimator = null
    }

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

class IndexedViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {
    companion object {
        fun fromParent(parent: ViewGroup): IndexedViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_integration_indexed_test_layout, parent, false)
            return IndexedViewHolder(root)
        }
    }
}
