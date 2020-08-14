package com.friendoye.recyclerxray.sample

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider
import com.friendoye.recyclerxray.sample.databinding.*


@SuppressLint("SetTextI18n")
class SmallViewHolder private constructor(
    private val binding: ItemSampleSmallBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType.Small>,
    XRayCustomParamsViewHolderProvider {

    override fun bind(item: ItemType.Small) {
        binding.textView.text = "Number: ${item.number}"
    }

    override fun provideCustomParams(): Map<String, Any?>? {
        return mapOf("Type" to "Small")
    }

    companion object {
        fun fromParent(parent: ViewGroup): SmallViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleSmallBinding.inflate(inflater)
            return SmallViewHolder(binding)
        }
    }
}

@SuppressLint("SetTextI18n")
class LargeViewHolder private constructor(
    private val binding: ItemSampleLargeBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType.Large> {

    override fun bind(item: ItemType.Large) {
        binding.textView.text = "String: ${item.string}"
    }

    companion object {
        fun fromParent(parent: ViewGroup): LargeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleLargeBinding.inflate(inflater)
            return LargeViewHolder(binding)
        }
    }
}

/*
 * Note: `Outer` class is introduced to test whether linking to file works,
 * when ViewHolder subclass is placed inside other class.
 */
class Outer {

    class WidestViewHolder private constructor(
        private val binding: ItemSampleWidestBinding
    ) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType.Widest>,
        XRayCustomParamsViewHolderProvider {

        override fun provideCustomParams(): Map<String, Any?>? {
            return mapOf("Type" to "Widest")
        }

        companion object {
            fun fromParent(parent: ViewGroup): WidestViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemSampleWidestBinding.inflate(inflater)
                return WidestViewHolder(binding)
            }
        }
    }
}

class EmptyViewHolder private constructor(
    private val binding: ItemSampleEmptyBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType.Empty> {

    companion object {
        fun fromParent(parent: ViewGroup): EmptyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleEmptyBinding.inflate(inflater)
            return EmptyViewHolder(binding)
        }
    }
}

class HorizontalRecyclerViewHolder private constructor(
    private val binding: ItemSampleHorizontalRecyclerBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<ItemType.HorizontalRecycler> {

    private val innerAdapter = InnerHorizontalAdapter()

    init {
        binding.innerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = innerAdapter
        }
    }

    override fun bind(item: ItemType.HorizontalRecycler) {
        innerAdapter.items = item.items
    }

    companion object {
        fun fromParent(parent: ViewGroup): HorizontalRecyclerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSampleHorizontalRecyclerBinding.inflate(inflater)
            return HorizontalRecyclerViewHolder(binding)
        }
    }
}