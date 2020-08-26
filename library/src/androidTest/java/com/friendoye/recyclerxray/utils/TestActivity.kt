package com.friendoye.recyclerxray.utils

import android.os.Bundle
import android.view.View
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.friendoye.recyclerxray.test.R

class TestActivity : ComponentActivity() {

    val testRecycler by lazy { findViewById<RecyclerView>(R.id.test_recycler_view) }

    var layoutManagerProvider: (View) -> LayoutManager = { rootView ->
        LinearLayoutManager(rootView.context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        testRecycler.apply {
            isNestedScrollingEnabled = false
            layoutManager = layoutManagerProvider(rootView)
            itemAnimator = null
            addItemDecoration(
                DividerItemDecoration(this@TestActivity, RecyclerView.VERTICAL).apply {
                    setDrawable(
                        ContextCompat.getDrawable(this@TestActivity, R.drawable.drawable_vert_divider)!!
                    )
                }
            )
        }
    }
}
