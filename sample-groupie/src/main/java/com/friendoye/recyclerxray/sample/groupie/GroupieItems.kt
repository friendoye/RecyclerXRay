package com.friendoye.recyclerxray.sample.groupie

import android.annotation.SuppressLint
import com.friendoye.recyclerxray.sample.shared.R
import com.friendoye.recyclerxray.sample.shared.databinding.ItemSampleLargeBinding
import com.friendoye.recyclerxray.sample.shared.databinding.ItemSampleSmallBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

@SuppressLint("SetTextI18n")
class SmallItem(val number: Int) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_sample_small

    override fun bind(vh: GroupieViewHolder, position: Int) {
        ItemSampleSmallBinding.bind(vh.itemView).apply {
            textView.text = "Number: $number"
        }
    }
}

@SuppressLint("SetTextI18n")
class LargeItem(val string: String) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_sample_large

    override fun bind(vh: GroupieViewHolder, position: Int) {
        ItemSampleLargeBinding.bind(vh.itemView).apply {
            textView.text = "String: $string"
        }
    }
}

class WidestItem : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_sample_widest

    override fun bind(vh: GroupieViewHolder, position: Int) {
        // Do nothing
    }
}

class EmptyItem : Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_sample_empty

    override fun bind(vh: GroupieViewHolder, position: Int) {
        // Do nothing
    }
}
