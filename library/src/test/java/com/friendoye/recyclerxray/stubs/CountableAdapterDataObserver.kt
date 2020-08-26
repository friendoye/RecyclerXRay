package com.friendoye.recyclerxray.stubs

import androidx.recyclerview.widget.RecyclerView

class CountableAdapterDataObserver constructor(
    internal var methodsInvokedCount: Int
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        methodsInvokedCount++
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        methodsInvokedCount++
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        methodsInvokedCount++
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        methodsInvokedCount++
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        methodsInvokedCount++
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        methodsInvokedCount++
    }
}
