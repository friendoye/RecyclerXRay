package com.friendoye.recyclerxray.xray

import android.graphics.Color
import androidx.annotation.ColorInt
import java.util.*
import kotlin.math.sqrt

internal val GOLDEN_RATIO_CONSTANT = (1.0 + sqrt(5.0)).toFloat() / 2

internal fun provideDefaultColorGeneratorRandom() = Random(0xB00BB00B)

/**
 * @implNote: For color generation uses approach with exploiting Golden Ratio
 * (more info: http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/).
 */
@ColorInt
internal fun Random.generateColor(): Int {
    var hue: Float = nextFloat() + GOLDEN_RATIO_CONSTANT
    hue %= 1
    hue *= 360
    return Color.HSVToColor(floatArrayOf(
        hue,  // hue
        0.5f, // saturation
        0.95f // value
    ))
}