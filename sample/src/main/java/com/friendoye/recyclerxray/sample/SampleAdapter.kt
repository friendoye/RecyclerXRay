package com.friendoye.recyclerxray.sample

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

class SampleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<ItemType> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return items[position].ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val type = ItemType.fromOrdinal(viewType)
            ?: throw IllegalStateException("Could not find ItemType for viewType = $viewType")
        return when (type) {
            ItemType.SMALL -> SmallViewHolder.fromParent(parent)
            ItemType.LARGE -> LargeViewHolder.fromParent(parent)
            ItemType.WIDEST -> Outer.WidestViewHolder.fromParent(parent)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as? Bindable<ItemType>
            ?: throw IllegalStateException("${holder.javaClass.simpleName} should implement Bindable!")
        val item = items[position]
        holder.bind(item)
    }
}