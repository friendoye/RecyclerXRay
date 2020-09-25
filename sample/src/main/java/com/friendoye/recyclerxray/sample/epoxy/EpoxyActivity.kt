package com.friendoye.recyclerxray.sample.epoxy

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.friendoye.recyclerxray.NestedXRaySettingsProvider
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.XRaySettings
import com.friendoye.recyclerxray.sample.R
import com.friendoye.recyclerxray.sample.databinding.ActivityEpoxyBinding
import com.friendoye.recyclerxray.sample.dip
import com.friendoye.recyclerxray.sample.groupie.EmptyItem
import com.friendoye.recyclerxray.sample.groupie.GroupieLinkProvider
import com.friendoye.recyclerxray.sample.groupie.LargeItem
import com.friendoye.recyclerxray.sample.groupie.SmallItem
import com.friendoye.recyclerxray.sample.groupie.WidestItem
import com.xwray.groupie.GroupAdapter
import kotlin.properties.Delegates

class EpoxyActivity  : AppCompatActivity() {

    internal var isFullDataInAdapter = true
        set(value) {
            field = value
            if (value) {
                controller.setData(SAMPLE_DATA_FULL)
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_exit_deep_purple_a200_36dp)
            } else {
                controller.setData(SAMPLE_DATA_PARTIAL)
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_deep_purple_a200_36dp)
            }
        }

    internal var binding by Delegates.notNull<ActivityEpoxyBinding>()

    private val controller = EpoxyController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpoxyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFullDataInAdapter = true
        binding.floatingActionButton.setOnClickListener {
            isFullDataInAdapter = !isFullDataInAdapter
        }

        setupRecyclerXRays()

        binding.sampleRecyclerViewView.apply {
            val spanCount = 3
            layoutManager = GridLayoutManager(context, spanCount).apply {
                controller.spanCount = spanCount
                spanSizeLookup = controller.spanSizeLookup
            }
            // Test RecyclerXRay
            adapter = RecyclerXRay.wrap(controller.adapter)
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
                listOf(EpoxyLinkProvider())
            )
            .build()
    }
}