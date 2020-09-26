package com.friendoye.recyclerxray.sample.groupie

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.NestedXRaySettingsProvider
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.XRaySettings
import com.friendoye.recyclerxray.sample.groupie.databinding.ActivityGroupieBinding
import com.friendoye.recyclerxray.sample.shared.R
import com.friendoye.recyclerxray.sample.shared.dip
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlin.properties.Delegates

class GroupieActivity : AppCompatActivity() {

    internal var isFullDataInAdapter = true
        set(value) {
            field = value
            if (value) {
                sampleAdapter.updateAsync(SAMPLE_DATA_FULL)
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_exit_deep_purple_a200_36dp)
            } else {
                sampleAdapter.updateAsync(SAMPLE_DATA_PARTIAL)
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_deep_purple_a200_36dp)
            }
        }

    internal var binding by Delegates.notNull<ActivityGroupieBinding>()

    private var sampleAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFullDataInAdapter = true
        binding.floatingActionButton.setOnClickListener {
            isFullDataInAdapter = !isFullDataInAdapter
        }

        setupRecyclerXRays()

        binding.sampleRecyclerViewView.apply {
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = SampleAdapterSpanLookup {
                    adapter as? GroupAdapter<*> ?: RecyclerXRay.unwrap(adapter!!)
                }
            }
            // Test RecyclerXRay
            adapter = RecyclerXRay.wrap(sampleAdapter)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // On "Swap" toggle all possible RecyclerXRay's
            R.id.swap -> toggleAllSecrets()
            else -> return false
        }
        return true
    }

    internal fun toggleAllSecrets() {
        RecyclerXRay.toggleSecrets()
    }

    private fun setupRecyclerXRays() {
        // Setup global RecyclerXRay
        RecyclerXRay.settings = XRaySettings.Builder()
            .withMinDebugViewSize(dip(100))
            .enableNestedRecyclersSupport(true)
            .withNestedXRaySettingsProvider(object : NestedXRaySettingsProvider {
                override fun provide(
                    nestedAdapter: RecyclerView.Adapter<*>
                ): XRaySettings? {
                    return XRaySettings.Builder().build()
                }
            })
            .withExtraLoggableLinkProviders(
                listOf(com.friendoye.recyclerxray.GroupieLinkProvider())
            )
            .build()
    }

    class SampleAdapterSpanLookup(
        private val adapterProvider: () -> GroupAdapter<*>
    ) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val item = adapterProvider().getItem(position)
            return when (item) {
                is SmallItem -> 1
                is LargeItem -> 2
                is WidestItem,
                is EmptyItem -> 3
                else -> throw IllegalStateException(
                    "Unknown item = $item for position = $position"
                )
            }
        }
    }
}
