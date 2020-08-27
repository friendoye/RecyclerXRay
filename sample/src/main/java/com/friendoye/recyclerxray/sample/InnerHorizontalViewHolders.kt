package com.friendoye.recyclerxray.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider
import com.friendoye.recyclerxray.sample.databinding.ItemInnerBoxBinding
import com.friendoye.recyclerxray.sample.databinding.ItemInnerCircleBinding

class BoxViewHolder private constructor(
    private val binding: ItemInnerBoxBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<InnerItemType.Box>,
    XRayCustomParamsViewHolderProvider {

    override fun bind(item: InnerItemType.Box) {
        // Do nothing
    }

    override fun provideCustomParams(): Map<String, Any?>? {
        return mapOf("Shape" to "Rectangle")
    }

    companion object {
        fun fromParent(parent: ViewGroup): BoxViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemInnerBoxBinding.inflate(inflater)
            return BoxViewHolder(binding)
        }
    }
}

class CircleViewHolder private constructor(
    private val binding: ItemInnerCircleBinding
) : RecyclerView.ViewHolder(binding.root), Bindable<InnerItemType.Circle>,
    XRayCustomParamsViewHolderProvider {

    override fun bind(item: InnerItemType.Circle) {
        // Do nothing
    }

    override fun provideCustomParams(): Map<String, Any?>? {
        return mapOf("Shape" to "Circle")
    }

    companion object {
        fun fromParent(parent: ViewGroup): CircleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemInnerCircleBinding.inflate(inflater)
            return CircleViewHolder(binding)
        }
    }
}
