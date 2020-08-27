package com.friendoye.recyclerxray.sample

import androidx.recyclerview.widget.DiffUtil

open class DiffCalculator<T>(
    var oldItems: List<T>? = null,
    var newItems: List<T>? = null
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newItems?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return checkEquality(oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return checkEquality(oldItemPosition, newItemPosition)
    }

    protected fun checkEquality(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition)
        val newItem = getNewItem(newItemPosition)
        return checkEquality(oldItem, newItem)
    }

    protected fun getOldItem(position: Int): T? {
        return oldItems?.get(position)
    }

    protected fun getNewItem(position: Int): T? {
        return newItems?.get(position)
    }

    protected fun checkEquality(oldItem: T?, newItem: T?): Boolean {
        return oldItem == newItem
    }
}
