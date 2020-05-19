package com.friendoye.recyclerxray

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.databinding.ItemSampleLargeBinding
import com.friendoye.recyclerxray.databinding.ItemSampleSmallBinding
import com.friendoye.recyclerxray.databinding.ItemSampleWidestBinding


class SmallViewHolder private constructor(
    private val binding: ItemSampleSmallBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType> {

    companion object {
        fun fromParent(parent: ViewGroup): SmallViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleSmallBinding.inflate(inflater)
            return SmallViewHolder(binding)
        }
    }
}

class LargeViewHolder private constructor(
    private val binding: ItemSampleLargeBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType> {

    companion object {
        fun fromParent(parent: ViewGroup): LargeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleLargeBinding.inflate(inflater)
            return LargeViewHolder(binding)
        }
    }
}

class WidestViewHolder private constructor(
    private val binding: ItemSampleWidestBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType> {

    companion object {
        fun fromParent(parent: ViewGroup): WidestViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleWidestBinding.inflate(inflater)
            return WidestViewHolder(binding)
        }
    }
}