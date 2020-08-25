package com.friendoye.recyclerxray.internal

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class OverlayHideController(
    private val ownerAdapter: RecyclerView.Adapter<*>,
    private val isInXRayModeProvider: () -> Boolean
) {

    private var _isOverlayHidden: MutableList<Boolean> = mutableListOf()
    public val isOverlayHidden: List<Boolean>
        get() = _isOverlayHidden

    private var dataObserver: RecyclerView.AdapterDataObserver? = null

    fun toggleHidden(position: Int) {
        _isOverlayHidden[position] = !_isOverlayHidden[position]
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                Log.i("DataChange", "onChanged()")
                recyclerView.handler.postAtFrontOfQueue {
                    _isOverlayHidden = (0..ownerAdapter.itemCount).map { false }.toMutableList()
                    Log.i("DataChange", _isOverlayHidden.joinToString())
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.i("DataChange", "onItemRangeChanged($positionStart, $itemCount)")
                // Do nothing, because it is not structural change
                if (!isInXRayModeProvider()) {
                    (0..itemCount).map { index ->
                        _isOverlayHidden[positionStart + index] = false
                    }
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                Log.i("DataChange", "onItemRangeChanged($positionStart, $itemCount, $payload)")
                // Do nothing, because it is not structural change
                if (!isInXRayModeProvider()) {
                    (0..itemCount).map { index ->
                        _isOverlayHidden[positionStart + index] = false
                    }
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                Log.i("DataChange", "onItemRangeInserted($positionStart, $itemCount)")
                repeat(itemCount) {
                    _isOverlayHidden.add(positionStart, false)
                }
                Log.i("DataChange", _isOverlayHidden.joinToString())
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                Log.i("DataChange", "onItemRangeRemoved($positionStart, $itemCount)")
                repeat(itemCount) {
                    _isOverlayHidden.removeAt(positionStart)
                }
                Log.i("DataChange", _isOverlayHidden.joinToString())
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                Log.i("DataChange", "onItemRangeMoved($fromPosition, $toPosition, $itemCount)")
                val areHidden = (0..itemCount).map { _isOverlayHidden[fromPosition] }
                _isOverlayHidden.addAll(toPosition, areHidden)
                repeat (itemCount) { _isOverlayHidden.removeAt(fromPosition) }
                Log.i("DataChange", _isOverlayHidden.joinToString())
            }
        }
        _isOverlayHidden = (0..ownerAdapter.itemCount).map { false }.toMutableList()
        dataObserver?.let {
            ownerAdapter.registerAdapterDataObserver(it)
        }
    }

    fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        dataObserver?.let {
            ownerAdapter.unregisterAdapterDataObserver(it)
        }
        dataObserver = null
    }
}