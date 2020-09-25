@file:SuppressLint("NonConstantResourceId")
package com.friendoye.recyclerxray.sample.epoxy

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.ModelView
import com.friendoye.recyclerxray.sample.EmptyViewHolder
import com.friendoye.recyclerxray.sample.R
import com.friendoye.recyclerxray.sample.databinding.ActivityEpoxyBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleEmptyBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleLargeBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleSmallBinding
import com.friendoye.recyclerxray.sample.databinding.ItemSampleWidestBinding
import kotlin.properties.Delegates

@SuppressLint("SetTextI18n")
@EpoxyModelClass(layout = R.layout.item_sample_small)
abstract class SmallModel : EpoxyModelWithHolder<SmallModel.Holder>() {
    @EpoxyAttribute
    @JvmField
    var number: Int? = null

    override fun bind(holder: Holder) {
        holder.bindings.textView.text = "Number: $number"
    }

    class Holder : EpoxyHolder() {
        var bindings by Delegates.notNull<ItemSampleSmallBinding>()

        override fun bindView(view: View) {
            bindings = ItemSampleSmallBinding.bind(view)
        }
    }
}

@SuppressLint("SetTextI18n")
@EpoxyModelClass(layout = R.layout.item_sample_large)
abstract class LargeModel : EpoxyModelWithHolder<LargeModel.Holder>() {
    @EpoxyAttribute
    @JvmField
    var string: String? = null

    override fun bind(holder: Holder) {
        holder.bindings.textView.text = "String: $string"
    }

    class Holder : EpoxyHolder() {
        var bindings by Delegates.notNull<ItemSampleLargeBinding>()

        override fun bindView(view: View) {
            bindings = ItemSampleLargeBinding.bind(view)
        }
    }
}

@ModelView(defaultLayout = R.layout.item_sample_widest)
class WidestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr)

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class EmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)