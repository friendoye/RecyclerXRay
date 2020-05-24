package com.friendoye.recyclerxray.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val adbToggleReceiver = AdbToggleReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var sampleAdapter: SampleAdapter = SampleAdapter().apply {
            items = SAMPLE_DATA
        }
        binding.sampleRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = SampleAdapterSpanLookup(sampleAdapter)
            }
            // Test RecyclerXRay
            adapter = RecyclerXRay.wrap(sampleAdapter)
        }
    }

    override fun onStart() {
        super.onStart()
        adbToggleReceiver.register()
    }

    override fun onStop() {
        adbToggleReceiver.unregister()
        super.onStop()
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
                ItemType.SMALL -> 1
                ItemType.LARGE -> 2
                ItemType.WIDEST -> 3
            }
        }
    }
}
