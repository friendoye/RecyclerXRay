@file:SuppressLint("NonConstantResourceId")
package com.friendoye.recyclerxray.sample.epoxy

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.ModelView
import com.friendoye.recyclerxray.sample.epoxy.R as epoxyR
import com.friendoye.recyclerxray.sample.shared.R
import com.friendoye.recyclerxray.sample.shared.databinding.ItemSampleLargeBinding
import com.friendoye.recyclerxray.sample.shared.databinding.ItemSampleSmallBinding
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

@ModelView(defaultLayout = epoxyR.layout.item_sample_widest_epoxy)
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
