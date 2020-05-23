package com.friendoye.recyclerxray

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.friendoye.recyclerxray.R
import kotlinx.android.synthetic.main.xray_item_debug_layout.view.*

class DefaultXRayDebugViewHolder :
    XRayDebugViewHolder {
    override fun provideView(parent: ViewGroup): View {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.xray_item_debug_layout, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bindView(debugView: View, result: XRayResult) {
        debugView.apply {
            debug_info_text_view.apply {
                text = """
                    ViewHolder class:
                    ${result.viewHolderClass.name}
                    ViewHolder type: ${result.viewHolderType}
                """.trimIndent()
                setBackgroundColor(result.color)
            }
        }
    }
}