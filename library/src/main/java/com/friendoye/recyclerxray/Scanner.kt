package com.friendoye.recyclerxray

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class Scanner(
    private val colorRandom: Random = provideDefaultColorGeneratorRandom()
) {

    private val holderInfoToColorMap: MutableMap<Pair<Class<*>, Int>, Int> = mutableMapOf()

    fun scan(holder: RecyclerView.ViewHolder, itemType: Int): XRayResult {
        return XRayResult(
            viewHolderClass = holder.javaClass,
            viewHolderType = itemType,
            color = getColorForItemType(holder.javaClass to itemType)
        )
    }

    @ColorInt
    private fun getColorForItemType(holderInfo: Pair<Class<*>, Int>): Int {
        return holderInfoToColorMap.getOrPut(holderInfo, colorRandom::generateColor)
    }
}