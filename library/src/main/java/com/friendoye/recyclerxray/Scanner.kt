package com.friendoye.recyclerxray

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class Scanner(
    private val colorRandom: Random = provideDefaultColorGeneratorRandom()
) {

    private val holderInfoToColorMap: MutableMap<Pair<Class<*>, Int>, Int> = mutableMapOf()

    fun scan(holder: RecyclerView.ViewHolder, itemType: Int,
             extraCustomParams: Map<String, Any?>?): XRayResult {
        return XRayResult(
            viewHolderClass = holder.javaClass,
            viewHolderType = itemType,
            color = getColorForItemType(holder.javaClass to itemType),
            customParams = holder.extractCustomParams(extraParams = extraCustomParams)
        )
    }

    private fun <T : RecyclerView.ViewHolder> T.extractCustomParams(
        extraParams: Map<String, Any?>? = null
    ): Map<String, Any?> {
        val holderParams = (this as? XRayCustomParamsViewHolderProvider)
            ?.provideCustomParams()
            ?.toMutableMap()
            ?: mutableMapOf()
        if (extraParams != null) {
            holderParams.putAll(extraParams)
        }
        return holderParams
    }

    @ColorInt
    private fun getColorForItemType(holderInfo: Pair<Class<*>, Int>): Int {
        return holderInfoToColorMap.getOrPut(holderInfo, colorRandom::generateColor)
    }
}