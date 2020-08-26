package com.friendoye.recyclerxray.stubs

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StubRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        throw NotImplementedError()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        throw NotImplementedError()
    }

    override fun getItemCount(): Int = 0
}
