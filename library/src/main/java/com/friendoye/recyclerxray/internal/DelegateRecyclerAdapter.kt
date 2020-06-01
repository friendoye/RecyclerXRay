package com.friendoye.recyclerxray.internal

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


internal open class DelegateRecyclerAdapter<T : RecyclerView.ViewHolder>(
    internal val decoratedAdapter: RecyclerView.Adapter<T>
) : RecyclerView.Adapter<T>() {

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        decoratedAdapter.unregisterAdapterDataObserver(observer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        return decoratedAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onViewDetachedFromWindow(holder: T) {
        decoratedAdapter.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return decoratedAdapter.itemCount
    }

    override fun getItemId(position: Int): Long {
        return decoratedAdapter.getItemId(position)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        decoratedAdapter.setHasStableIds(hasStableIds)
    }

    override fun onFailedToRecycleView(holder: T): Boolean {
        return decoratedAdapter.onFailedToRecycleView(holder)
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        return decoratedAdapter.onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: T, position: Int, payloads: List<Any>) {
        return decoratedAdapter.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return decoratedAdapter.getItemViewType(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        decoratedAdapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        decoratedAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewRecycled(holder: T) {
        decoratedAdapter.onViewRecycled(holder)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        decoratedAdapter.registerAdapterDataObserver(observer)
    }

    override fun onViewAttachedToWindow(holder: T) {
        decoratedAdapter.onViewAttachedToWindow(holder)
    }
}