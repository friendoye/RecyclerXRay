package com.friendoye.recyclerxray.xray

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.sqrt


/**
 * @implNote: For color generation was used approach with exploiting
 * Golden Ratio (more info: http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/).
 */
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
        return holderInfoToColorMap.getOrPut(holderInfo, this::generateColor)
    }

    @ColorInt
    private fun generateColor(): Int {
        val goldenRatioMultiplier = (1.0 + sqrt(5.0)) / 2.0
        var hue: Float = colorRandom.nextFloat() + goldenRatioMultiplier.toFloat()
        hue %= 1
        hue *= 360
        return Color.HSVToColor(floatArrayOf(
            hue, // hue
            0.5f, // saturation
            0.95f // value
        ))
    }
}