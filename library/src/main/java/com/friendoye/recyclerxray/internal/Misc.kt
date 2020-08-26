package com.friendoye.recyclerxray.internal

import android.animation.AnimatorInflater
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.children
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.R
import java.lang.ref.WeakReference
import java.util.Random
import kotlin.math.sqrt

internal val GOLDEN_RATIO_CONSTANT = (1.0 + sqrt(5.0)).toFloat() / 2
internal val DEFAULT_LOG_TAG = "RecyclerXRay"
internal val DEFAULT_INTERNAL_LOG_TAG = "XRayInternal"

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
            0.5f, // saturation
            0.95f // value
        ))
    }
}

internal fun <T : RecyclerView.ViewHolder> T.isWrappedByXRay(): Boolean {
    val xRayWrapperContainer = itemView
    return xRayWrapperContainer.getTag(R.id.original_item_view_key) != null
}

internal fun <T : RecyclerView.ViewHolder> T.replaceItemView(
    xRayWrapperContainer: View,
    xRayApiId: Long? = _xRayApiId,
    xRayDebugLabel: String? = _xRayDebugLabel
): T = apply {
    xRayWrapperContainer.setTag(R.id.original_item_view_key, itemView.asWeakRef())
    if (xRayApiId != null) {
        xRayWrapperContainer.setTag(R.id.x_ray_api_id, xRayApiId)
    }
    xRayWrapperContainer.setTag(R.id.x_ray_debug_label, xRayDebugLabel)
    val field = RecyclerView.ViewHolder::class.java.getDeclaredField("itemView")
    field.isAccessible = true
    field.set(this, xRayWrapperContainer)
}

internal fun <T : RecyclerView.ViewHolder> T.rebindInfo(
    xRayApiId: Long? = _xRayApiId,
    xRayDebugLabel: String? = _xRayDebugLabel
): T = apply {
    if (isWrappedByXRay()) {
        val xRayWrapperContainer = itemView
        xRayWrapperContainer.setTag(R.id.x_ray_api_id, xRayApiId)
        xRayWrapperContainer.setTag(R.id.x_ray_debug_label, xRayDebugLabel)
    } else {
        Log.d(DEFAULT_INTERNAL_LOG_TAG, "Couldn't rebind info, because $this is not wrapped...")
    }
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

internal inline val <T : RecyclerView.ViewHolder> T.originalItemView: View
    get() {
        return (itemView.getTag(R.id.original_item_view_key) as? WeakReference<View>)
            ?.get() ?: itemView
    }

internal inline val <T : RecyclerView.ViewHolder> T._xRayApiId: Long?
    get() = itemView.getTag(R.id.x_ray_api_id) as? Long

internal inline val <T : RecyclerView.ViewHolder> T._xRayDebugLabel: String?
    get() = itemView.getTag(R.id.x_ray_debug_label) as? String

internal val <T : RecyclerView.ViewHolder> T.innerRecycler: RecyclerView?
    get() = originalItemView.findFirstView<RecyclerView>()

internal val <T : RecyclerView.ViewHolder> T.innerAdapter: RecyclerView.Adapter<*>?
    get() = innerRecycler?.adapter

internal fun <T> T.asWeakRef(): WeakReference<T> = WeakReference(this)

internal inline fun <reified T> View.findFirstView(): T? = findFirstView(T::class.java)

internal fun <T> View.findFirstView(clazz: Class<T>): T? {
    if (clazz.isInstance(this)) {
        return this as T
    } else if (this is ViewGroup) {
        return children.map { it.findFirstView(clazz) }
            .filter { it != null }
            .firstOrNull()
    } else {
        return null
    }
}

internal fun View.startGripAnimation() {
    val animator = AnimatorInflater
        .loadAnimator(context, R.animator.grip_animator)
        .apply {
            setTarget(this@startGripAnimation)
            interpolator = AccelerateDecelerateInterpolator()
        }
    animator.start()
}

internal fun RecyclerView.Adapter<*>.checkIsWrappedCorrectly(): Boolean {
    val recyclerAdapter = this
    val isFullyWrappedOutside = recyclerAdapter is ScannableRecyclerAdapter
    val isFullyWrappedInside = recyclerAdapter is ConcatAdapter &&
            (recyclerAdapter.hasIsolateViewTypes || recyclerAdapter.adapters.all { it is ScannableRecyclerAdapter })

    return isFullyWrappedOutside || isFullyWrappedInside
}

private val ConcatAdapter.hasIsolateViewTypes: Boolean
    get() {
        val controllerField = ConcatAdapter::class.java.getDeclaredField("mController")
        controllerField.isAccessible = true
        val controller = controllerField.get(this)
        val viewTypeStorageField = controller::class.java.getDeclaredField("mViewTypeStorage")
        viewTypeStorageField.isAccessible = true
        val viewTypeStorage = viewTypeStorageField.get(controller)
        val storageType = viewTypeStorage::class.java.simpleName
        return storageType == "IsolatedViewTypeStorage"
    }
