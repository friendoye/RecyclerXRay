package com.friendoye.recyclerxray.internal

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

internal class OverlayHideController(
    private val ownerAdapter: RecyclerView.Adapter<*>,
    private val isInXRayModeProvider: () -> Boolean
) {

    private var _isOverlayHidden: MutableList<Boolean> = mutableListOf()
    val isOverlayHidden: List<Boolean>
        get() = _isOverlayHidden

    private var dataObserver: RecyclerView.AdapterDataObserver? = null

    fun toggleHidden(position: Int) {
        _isOverlayHidden[position] = !_isOverlayHidden[position]
        Log.d(DEFAULT_INTERNAL_LOG_TAG, _isOverlayHidden.joinToString())
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onChanged()")
                recyclerView.handler.postAtFrontOfQueue {
                    _isOverlayHidden = (0 until ownerAdapter.itemCount).map { false }.toMutableList()
                    logOverlayHiddenFlagsState()
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onItemRangeChanged($positionStart, $itemCount)")
                if (!isInXRayModeProvider()) {
                    (0 until itemCount).map { index ->
                        _isOverlayHidden[positionStart + index] = false
                    }
                    logOverlayHiddenFlagsState()
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onItemRangeChanged($positionStart, $itemCount, $payload)")
                if (!isInXRayModeProvider()) {
                    (0 until itemCount).map { index ->
                        _isOverlayHidden[positionStart + index] = false
                    }
                    logOverlayHiddenFlagsState()
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onItemRangeInserted($positionStart, $itemCount)")
                repeat(itemCount) {
                    _isOverlayHidden.add(positionStart, false)
                }
                logOverlayHiddenFlagsState()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onItemRangeRemoved($positionStart, $itemCount)")
                repeat(itemCount) {
                    _isOverlayHidden.removeAt(positionStart)
                }
                logOverlayHiddenFlagsState()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onItemRangeMoved($fromPosition, $toPosition, $itemCount)")
                val areHidden = (0..itemCount).map { _isOverlayHidden[fromPosition] }
                _isOverlayHidden.addAll(toPosition, areHidden)
                repeat(itemCount) { _isOverlayHidden.removeAt(fromPosition) }
                logOverlayHiddenFlagsState()
            }
        }

        Log.d(DEFAULT_INTERNAL_LOG_TAG, "OverlayHideController: onAttachedToRecyclerView()")
        _isOverlayHidden = (0 until ownerAdapter.itemCount).map { false }.toMutableList()
        logOverlayHiddenFlagsState()

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

    private fun logOverlayHiddenFlagsState() {
        Log.d(DEFAULT_INTERNAL_LOG_TAG, _isOverlayHidden.joinToString())
    }
}
