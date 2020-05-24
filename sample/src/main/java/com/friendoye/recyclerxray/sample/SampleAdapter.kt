package com.friendoye.recyclerxray.sample

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsAdapterProvider
import java.lang.IllegalStateException

class SampleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    XRayCustomParamsAdapterProvider {

    var items: List<ItemType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ItemType.Small -> 1
            is ItemType.Large -> 2
            is ItemType.Widest -> 3
        }
    }

    override fun provideCustomParams(position: Int): Map<String, Any?>? {
        return when (getItemViewType(position)) {
            1 -> mapOf(
                "Number" to (items[position] as ItemType.Small).number
            )
            2 -> mapOf(
                "String" to (items[position] as ItemType.Large).string
            )
            3 -> null
            else -> null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> SmallViewHolder.fromParent(parent)
            2 -> LargeViewHolder.fromParent(parent)
            3 -> Outer.WidestViewHolder.fromParent(parent)
            else -> throw IllegalStateException("Could not find ItemType for viewType = $viewType")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            1 -> (holder as SmallViewHolder).bind(item as ItemType.Small)
            2 -> (holder as LargeViewHolder).bind(item as ItemType.Large)
            3 -> (holder as Outer.WidestViewHolder).bind(item as ItemType.Widest)
        }
    }
}