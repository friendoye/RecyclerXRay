package com.friendoye.recyclerxray.internal

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.friendoye.recyclerxray.*
import kotlinx.android.synthetic.main.xray_item_debug_layout.view.*

internal class DefaultXRayDebugViewHolder : XRayDebugViewHolder {
    override fun provideView(parent: ViewGroup): View {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.xray_item_debug_layout, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bindView(debugView: View, result: XRayResult) {
        debugView.apply {
            debug_info_text_view.apply {
                text = prepareDebugText(result)
                setBackgroundColor(result.color)
            }

            alpha = 1.0f
            setOnClickListener {
                val loggableLinkToFile = result.viewHolderClass.getLoggableLinkToFileWithClass()
                Log.i(DEFAULT_LOG_TAG, loggableLinkToFile ?: "...")
                if (result.isViewVisibleForUser) {
                    it.alpha = if (it.alpha == 1.0f) 0.0f else 1.0f
                } else {
                    debug_info_text_view.startGripAnimation()
                }
            }
        }
    }

    private fun prepareDebugText(result: XRayResult): String {
        var textToShow = """
            ViewHolder class:
            ${result.viewHolderClass.name}
            ViewHolder type: ${result.viewHolderType}
        """.trimIndent()
        if (result.customParams.isNotEmpty()) {
            textToShow += "\n" + """
                Custom params:
                ${result.customParams.entries.joinToString(separator = "\n")}
            """.trimIndent()
        }
        if (!result.isViewVisibleForUser) {
            textToShow += "\n" + """
                    <Empty View>
            """.trimIndent()
        }
        return textToShow
    }

    private fun View.startGripAnimation() {
        val animator = AnimatorInflater
            .loadAnimator(context,
                R.animator.grip_animator
            ).apply {
                setTarget(this@startGripAnimation)
                interpolator = AccelerateDecelerateInterpolator()
            }
        animator.start()
    }
}