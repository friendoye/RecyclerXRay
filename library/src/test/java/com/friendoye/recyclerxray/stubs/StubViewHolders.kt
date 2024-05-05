package com.friendoye.recyclerxray.stubs

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider

class TestViewHolder1(view: View) : RecyclerView.ViewHolder(view)
class TestViewHolder2(view: View) : RecyclerView.ViewHolder(view)
class TestViewHolder3(view: View) : RecyclerView.ViewHolder(view)
class TestViewHolder4(view: View) : RecyclerView.ViewHolder(view)

class XRayCustomParamsDelegateViewHolder(
    paramsProviderDelegate: XRayCustomParamsViewHolderProvider,
    view: View,
) : RecyclerView.ViewHolder(view), XRayCustomParamsViewHolderProvider by paramsProviderDelegate
