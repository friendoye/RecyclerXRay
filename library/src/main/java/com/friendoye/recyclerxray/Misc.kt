package com.friendoye.recyclerxray

import android.graphics.Color
import androidx.annotation.ColorInt
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.sqrt

internal val GOLDEN_RATIO_CONSTANT = (1.0 + sqrt(5.0)).toFloat() / 2
internal val DEFAULT_LOG_TAG = "RecyclerXRay"

internal fun provideDefaultColorGeneratorRandom() = Random(0xB00BB00B)

/**
 * @implNote: For color generation uses approach with exploiting Golden Ratio
 * (more info: http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/).
 */
internal fun Random.generateColorSequence(): Sequence<Int> {
    var hue = nextFloat()
    return generateSequence {
        hue += GOLDEN_RATIO_CONSTANT
        hue %= 1
        Color.HSVToColor(floatArrayOf(
            360 * hue, // hue
            0.5f,      // saturation
            0.95f      // value
        ))
    }
}

internal fun Class<*>.getLoggableLinkToFileWithClass(): String? {
    try {
        val constructor = constructors.first()
        constructor.isAccessible = true
        val argsTypes = constructor.parameterTypes
        val array = Array(argsTypes.size) { index -> argsTypes[index].cast(null) }
        constructor.newInstance(*array)
    } catch (e: Exception) {
        val linkToClass = e.cause?.stackTrace?.get(0)?.run {
            "$fileName:$lineNumber"
        }
        return "$simpleName($linkToClass)"
    }
    return null
}

internal fun <T> T.asWeakRef(): WeakReference<T> = WeakReference(this)