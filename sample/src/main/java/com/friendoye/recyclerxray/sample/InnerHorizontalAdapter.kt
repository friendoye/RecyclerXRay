package com.friendoye.recyclerxray.sample

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.sample.InnerItemType.Companion.getOrdinal

class InnerHorizontalAdapter(
    givenItems: List<InnerItemType> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = givenItems
        set(value) {
            val diffCallback = DiffCalculator(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return items[position].getOrdinal()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (InnerItemType.fromOrdinal(viewType)) {
            type<InnerItemType.Box>() -> BoxViewHolder.fromParent(parent)
            type<InnerItemType.Circle>() -> CircleViewHolder.fromParent(parent)
            else -> throw IllegalStateException("Could not find ItemType for viewType = $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (InnerItemType.fromOrdinal(getItemViewType(position))) {
            type<InnerItemType.Box>() -> (holder as BoxViewHolder).bind(item as InnerItemType.Box)
            type<InnerItemType.Circle>() -> (holder as CircleViewHolder).bind(item as InnerItemType.Circle)
        }
    }
}