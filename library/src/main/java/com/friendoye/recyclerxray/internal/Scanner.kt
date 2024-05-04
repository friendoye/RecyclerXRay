package com.friendoye.recyclerxray.internal

import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.XRayCustomParamsViewHolderProvider
import com.friendoye.recyclerxray.XRayResult
import java.util.Random
import kotlin.LazyThreadSafetyMode.NONE

internal class Scanner(
    private val colorRandom: Random = provideDefaultColorGeneratorRandom(),
) {

    private val holderInfoToColorMap: MutableMap<Pair<Class<*>, Int>, Int> = mutableMapOf()
    private val colorsIterator by lazy(NONE) {
        colorRandom.generateColorSequence().iterator()
    }

    fun scan(
        holder: RecyclerView.ViewHolder,
        itemType: Int,
        extraCustomParams: Map<String, Any?>?,
        originalItemView: View,
    ): XRayResult {
        return XRayResult(
            viewHolderClass = holder.javaClass,
            viewHolderType = itemType,
            color = getColorForItemType(holder.javaClass to itemType),
            customParams = holder.extractCustomParams(extraParams = extraCustomParams),
            viewWidth = originalItemView.width,
            viewHeight = originalItemView.height,
            viewVisibility = originalItemView.visibility,
        )
    }

    private fun <T : RecyclerView.ViewHolder> T.extractCustomParams(
        extraParams: Map<String, Any?>? = null,
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
        return holderInfoToColorMap.getOrPut(holderInfo, colorsIterator::next)
    }
}
