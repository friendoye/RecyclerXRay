package com.friendoye.recyclerxray.stubs

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsAdapterProvider

class TestAdapter(
    private val itemsCount: Int,
    private val paramsDelegate: XRayCustomParamsAdapterProvider,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    XRayCustomParamsAdapterProvider by paramsDelegate {

    override fun getItemCount(): Int = itemsCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(View(parent.context))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Do nothing
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
