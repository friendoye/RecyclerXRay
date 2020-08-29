package com.friendoye.recyclerxray.utils

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule

fun Context.dip(value: Int): Int = (value * (resources?.displayMetrics?.density ?: 0f)).toInt()
fun Context.dip(value: Float): Int = (value * (resources?.displayMetrics?.density ?: 0f)).toInt()

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

    private fun checkEquality(oldItemPosition: Int, newItemPosition: Int): Boolean {
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

fun ActivityTestRule<*>.ensureAllViewHoldersBind(recyclerView: RecyclerView) {
    runOnUiThread {
        recyclerView.scrollToPosition(
            recyclerView.adapter?.itemCount?.minus(1)?.coerceAtLeast(0)
                ?: 0
        )
    }

    runOnUiThread {
        recyclerView.scrollToPosition(0)
    }
}
