package com.friendoye.recyclerxray

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.friendoye.recyclerxray.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sampleAdapter = SampleAdapter().apply {
            items = SAMPLE_DATA
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = SampleAdapterSpanLookup(sampleAdapter)
            }
            adapter = sampleAdapter
        }
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
