package com.friendoye.recyclerxray.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider
import com.friendoye.recyclerxray.test.R

internal fun createInnerTestAdapter(
    vararg items: InnerTestItemType,
    useDiffUtils: Boolean = true
): InnerRecyclerTestAdapter {
    return InnerRecyclerTestAdapter(useDiffUtils = useDiffUtils).apply {
        this.items = items.toList()
    }
}

class InnerRecyclerTestAdapter(
    var useDiffUtils: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<InnerTestItemType> = emptyList()
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

    fun changeItems(vararg items: InnerTestItemType) {
        this.items = items.toList()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is InnerTestItemType.Box -> 1
            is InnerTestItemType.Circle -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> BoxViewHolder.fromParent(parent)
            2 -> CircleViewHolder.fromParent(parent)
            else -> throw IllegalStateException("Could not find InnerTestItemType for viewType = $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Do nothing
    }
}

sealed class InnerTestItemType {
    object Box : InnerTestItemType()
    object Circle : InnerTestItemType()
}

class BoxViewHolder private constructor(root: View) :
    RecyclerView.ViewHolder(root),
    XRayCustomParamsViewHolderProvider {

    override fun provideCustomParams(): Map<String, Any?>? {
        return mapOf("Shape" to "Rectangle")
    }

    companion object {
        fun fromParent(parent: ViewGroup): BoxViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inner_box_test_layout, parent, false)
            return BoxViewHolder(root)
        }
    }
}

class CircleViewHolder private constructor(root: View) :
    RecyclerView.ViewHolder(root),
    XRayCustomParamsViewHolderProvider {

    override fun provideCustomParams(): Map<String, Any?>? {
        return mapOf("Shape" to "Circle")
    }

    companion object {
        fun fromParent(parent: ViewGroup): CircleViewHolder {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_inner_circle_test_layout, parent, false)
            return CircleViewHolder(root)
        }
    }
}
