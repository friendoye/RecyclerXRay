package com.friendoye.recyclerxray.sample

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider
import com.friendoye.recyclerxray.sample.databinding.ItemSampleEmptyBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleLargeBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleSmallBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleWidestBinding


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