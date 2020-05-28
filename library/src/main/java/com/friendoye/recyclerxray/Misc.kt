package com.friendoye.recyclerxray

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
        val array = Array(argsTypes.size) { index -> argsTypes[index].createInstance() }
        constructor.newInstance(*array)
    } catch (e: Exception) {
        var linkToClass: String?
        // TODO: Think about Kotlin non-null check: Intrinsics.checkParameterIsNotNull
        linkToClass = canonicalName?.run {
            e.cause?.stackTrace?.find { it.toString().contains(this) }?.run {
                "$fileName:$lineNumber"
            }
        }
        if (linkToClass == null) {
            linkToClass = e.cause?.stackTrace?.get(0)?.run {
                "$fileName:$lineNumber"
            }
        }
        if (linkToClass == null || linkToClass.startsWith("RecyclerView.java")) {
            linkToClass = e.cause?.stackTrace?.get(1)?.run {
                "$fileName:$lineNumber"
            }
        }
        return "$simpleName($linkToClass)"
    }
    return null
}

@Suppress("UNCHECKED_CAST")
internal fun <T> Class<T>.createInstance(): T? {
    val value = when (this) {
        Byte::class.java -> 0.toByte()
        Short::class.java -> 0.toShort()
        Int::class.java -> 0
        Long::class.java -> 0L
        Float::class.java -> 0f
        Double::class.java -> 0.0
        Char::class.java -> ' '
        String::class.java -> ""
        Boolean::class.java -> false
        else -> null
    }
    return value as T?
}

internal fun <T : RecyclerView.ViewHolder> T.replaceItemView(xRayWrapperContainer: View): T = apply {
    xRayWrapperContainer.setTag(R.id.original_item_view_key, itemView.asWeakRef())
    val field = RecyclerView.ViewHolder::class.java.getDeclaredField("itemView")
    field.isAccessible = true
    field.set(this, xRayWrapperContainer)
}

internal inline fun <T : RecyclerView.ViewHolder> T.originalItemViewContext(action: (T) -> Unit): T = apply {
    val xRayWrapperContainer = itemView
    val originalItemView = (itemView.getTag(R.id.original_item_view_key) as? WeakReference<View>)
        ?.get()
    val field = RecyclerView.ViewHolder::class.java.getDeclaredField("itemView")
    field.isAccessible = true
    field.set(this, originalItemView)
    action(this)
    replaceItemView(xRayWrapperContainer)
}

internal fun <T> T.asWeakRef(): WeakReference<T> = WeakReference(this)