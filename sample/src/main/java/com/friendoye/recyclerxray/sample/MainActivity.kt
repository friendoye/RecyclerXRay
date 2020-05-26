package com.friendoye.recyclerxray.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.friendoye.recyclerxray.AdbToggleReceiver
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.XRaySettings
import com.friendoye.recyclerxray.sample.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val adbToggleReceiver = AdbToggleReceiver(this)
    private var isFullDataInAdapter = true
        set(value) {
            field = value
            if (value) {
                sampleAdapter.items = SAMPLE_DATA_FULL
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_exit_deep_purple_a200_36dp)
            } else {
                sampleAdapter.items = SAMPLE_DATA_PARTIAL
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_deep_purple_a200_36dp)
            }
        }

    private var binding by Delegates.notNull<ActivityMainBinding>()
    private var sampleAdapter = SampleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFullDataInAdapter = true
        binding.floatingActionButton.setOnClickListener {
            isFullDataInAdapter = !isFullDataInAdapter
        }

        RecyclerXRay.settings = XRaySettings.Builder()
            .withMinDebugViewSize(dip(100))
            .build()

        binding.sampleRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = SampleAdapterSpanLookup(sampleAdapter)
            }
            // Test RecyclerXRay
            adapter = RecyclerXRay.wrap(sampleAdapter)
        }

        lifecycle.addObserver(adbToggleReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.swap -> RecyclerXRay.toggleSecrets()
            else -> return false
        }
        return true
    }

    class SampleAdapterSpanLookup(
        private val adapter: SampleAdapter
    ) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val type = adapter.items[position]
            return when (type) {
                is ItemType.Small -> 1
                is ItemType.Large -> 2
                is ItemType.Widest -> 3
                is ItemType.Empty -> 3
            }
        }
    }
}
