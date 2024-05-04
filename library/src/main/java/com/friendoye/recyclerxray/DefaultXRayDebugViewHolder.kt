package com.friendoye.recyclerxray

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.friendoye.recyclerxray.databinding.XrayItemDebugLayoutBinding
import com.friendoye.recyclerxray.internal.startGripAnimation

/**
 * Default implementation of [XRayDebugViewHolder]. Used by default for all [LocalRecyclerXRay]'s.
 */
open class DefaultXRayDebugViewHolder : XRayDebugViewHolder {
    override fun provideView(parent: ViewGroup): View {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.xray_item_debug_layout, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bindView(debugView: View, result: XRayResult) {
        XrayItemDebugLayoutBinding.bind(debugView).apply {
            debugInfoTextView.apply {
                text = prepareDebugText(result)
                setBackgroundColor(result.color)
            }
        }
    }

    override fun onEmptyViewClick(debugView: View, result: XRayResult) {
        XrayItemDebugLayoutBinding.bind(debugView).apply {
            debugInfoTextView.startGripAnimation()
        }
    }

    protected open fun prepareDebugText(result: XRayResult): String {
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
}
