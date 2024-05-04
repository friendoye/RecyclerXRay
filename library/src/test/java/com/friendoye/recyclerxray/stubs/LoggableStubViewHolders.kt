package com.friendoye.recyclerxray.stubs

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ViewBindingViewHolder(
    private val viewBinding: ViewBinding,
) : RecyclerView.ViewHolder(viewBinding.root)

class PrimitiveArgsViewHolder(
    private val integer: Int,
    private val string: String,
    private val boolean: Boolean,
    private val nullableInteger: Int?,
    private val nullableString: String?,
    private val nullableBoolean: Boolean?,
    private val nullableAny: Any?,
    view: View,
) : RecyclerView.ViewHolder(view)

class CustomArgsViewHolder(
    private val first: FirstCustomClass,
    private val second: SecondCustomClass,
    view: View,
) : RecyclerView.ViewHolder(view)
