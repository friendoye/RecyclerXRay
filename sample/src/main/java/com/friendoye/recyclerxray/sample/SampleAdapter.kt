package com.friendoye.recyclerxray.sample

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsAdapterProvider
import com.friendoye.recyclerxray.sample.ItemType.Companion.getOrdinal

class SampleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    XRayCustomParamsAdapterProvider {

    var items: List<ItemType> = emptyList()
        set(value) {
            val diffCallback = object : DiffCalculator<ItemType>(field, value) {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = getOldItem(oldItemPosition)
                    val newItem = getNewItem(newItemPosition)

                    // To animate change of horizontal RecyclerView content,
                    // we treat all ItemType.HorizontalRecycler items as the same.
                    if (oldItem is ItemType.HorizontalRecycler &&
                        newItem is ItemType.HorizontalRecycler) {
                        return true
                    } else {
                        return super.areItemsTheSame(oldItemPosition, newItemPosition)
                    }
                }
            }
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return items[position].getOrdinal()
    }

    override fun provideCustomParams(position: Int): Map<String, Any?>? {
        return when (ItemType.fromOrdinal(getItemViewType(position))) {
            type<ItemType.Small>() -> mapOf(
                "Number" to (items[position] as ItemType.Small).number
            )
            type<ItemType.Large>() -> mapOf(
                "String" to (items[position] as ItemType.Large).string
            )
            else -> null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ItemType.fromOrdinal(viewType)) {
            type<ItemType.Small>() -> SmallViewHolder.fromParent(parent)
            type<ItemType.Large>() -> LargeViewHolder.fromParent(parent)
            type<ItemType.Widest>() -> Outer.WidestViewHolder.fromParent(parent)
            type<ItemType.Empty>() -> EmptyViewHolder.fromParent(parent)
            type<ItemType.HorizontalRecycler>() -> HorizontalRecyclerViewHolder.fromParent(parent)
            else -> throw IllegalStateException("Could not find ItemType for viewType = $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (ItemType.fromOrdinal(getItemViewType(position))) {
            type<ItemType.Small>() -> (holder as SmallViewHolder).bind(item as ItemType.Small)
            type<ItemType.Large>() -> (holder as LargeViewHolder).bind(item as ItemType.Large)
            type<ItemType.Widest>() -> (holder as Outer.WidestViewHolder).bind(item as ItemType.Widest)
            type<ItemType.Empty>() -> (holder as EmptyViewHolder).bind(item as ItemType.Empty)
            type<ItemType.HorizontalRecycler>() -> (holder as HorizontalRecyclerViewHolder)
                .bind(item as ItemType.HorizontalRecycler)
        }
    }
}
