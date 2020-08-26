package com.friendoye.recyclerxray.utils

import android.os.Bundle
import android.view.View
import androidx.core.app.ComponentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.test.R

class RecyclingTestActivity : ComponentActivity() {

    val testRecycler by lazy { findViewById<RecyclerView>(R.id.test_recycler_view) }

    var layoutManagerProvider: (View) -> RecyclerView.LayoutManager = { rootView ->
        LinearLayoutManager(rootView.context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycling_activity_test)
        testRecycler.apply {
            layoutManager = layoutManagerProvider(rootView)
            itemAnimator = null
            addItemDecoration(
                DividerItemDecoration(this@RecyclingTestActivity, RecyclerView.VERTICAL)
            )
        }
    }
}