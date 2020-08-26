package com.friendoye.recyclerxray.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendoye.recyclerxray.AdbToggleReceiver
import com.friendoye.recyclerxray.DefaultXRayDebugViewHolder
import com.friendoye.recyclerxray.LocalRecyclerXRay
import com.friendoye.recyclerxray.NestedXRaySettingsProvider
import com.friendoye.recyclerxray.RecyclerXRay
import com.friendoye.recyclerxray.XRayResult
import com.friendoye.recyclerxray.XRaySettings
import com.friendoye.recyclerxray.sample.databinding.ActivityMainBinding
import kotlin.properties.Delegates

private val localRecyclerXRay = LocalRecyclerXRay()

class MainActivity : AppCompatActivity() {

    internal var isFullDataInAdapter = true
        set(value) {
            field = value
            if (value) {
                sampleAdapter.items = SAMPLE_DATA_FULL
                horizontalRecyclerAdapter.items = INNER_DATA_FULL
                localSampleAdapter.items = LOCAL_SAMPLE_DATA_FULL
                horizontalRecyclerWithAdapterChangeAdapter.items = INNER_DATA_ADAPTER_CHANGE_FULL
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_exit_deep_purple_a200_36dp)
            } else {
                sampleAdapter.items = SAMPLE_DATA_PARTIAL
                horizontalRecyclerAdapter.items = INNER_DATA_PARTIAL
                localSampleAdapter.items = LOCAL_SAMPLE_DATA_PARTIAL
                horizontalRecyclerWithAdapterChangeAdapter.items = INNER_DATA_ADAPTER_CHANGE_PARTIAL
                binding.floatingActionButton
                    .setImageResource(R.drawable.baseline_fullscreen_deep_purple_a200_36dp)
            }
        }

    internal var binding by Delegates.notNull<ActivityMainBinding>()

    private var sampleAdapter = SampleAdapter()
    private var horizontalRecyclerAdapter = SampleAdapter()
    private var localSampleAdapter = SampleAdapter()
    private var horizontalRecyclerWithAdapterChangeAdapter = SampleAdapter()

    private val adbToggleReceiverForAll = AdbToggleReceiver(this, intentAction = "xray-toggle-all", recyclerXRays = listOf(RecyclerXRay, localRecyclerXRay))
    private val adbToggleReceiverForGlobal = AdbToggleReceiver(this, intentAction = "xray-toggle-global")
    private val adbToggleReceiverForLocal = AdbToggleReceiver(this, intentAction = "xray-toggle-local", recyclerXRays = listOf(localRecyclerXRay))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFullDataInAdapter = true
        binding.floatingActionButton.setOnClickListener {
            isFullDataInAdapter = !isFullDataInAdapter
        }

        setupRecyclerXRays()

        binding.sampleRecyclerViewView.apply {
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = SampleAdapterSpanLookup {
                    adapter as? ConcatAdapter ?: RecyclerXRay.unwrap(adapter!!)
                }
            }
            // Test RecyclerXRay
            adapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    RecyclerXRay.wrap(sampleAdapter),
                    RecyclerXRay.wrap(horizontalRecyclerWithAdapterChangeAdapter),
                    localRecyclerXRay.wrap(localSampleAdapter),
                    RecyclerXRay.wrap(horizontalRecyclerAdapter)
                )
            )
            sampleAdapter = RecyclerXRay.unwrap((adapter as ConcatAdapter).adapters[0])
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
        adbToggleReceiverForAll.toggleSecrets()
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
            .build()
        // Setup local RecyclerXRay
        localRecyclerXRay.settings = XRaySettings.Builder()
            .withDefaultXRayDebugViewHolder(object : DefaultXRayDebugViewHolder() {
                override fun prepareDebugText(result: XRayResult): String {
                    return """
                        ${super.prepareDebugText(result)}
                        <From local RecyclerXRay>
                    """.trimIndent()
                }
            })
            .build()
        // Setup ability to toggle XRay mode via adb
        lifecycle.addObserver(adbToggleReceiverForAll)
        lifecycle.addObserver(adbToggleReceiverForGlobal)
        lifecycle.addObserver(adbToggleReceiverForLocal)
    }

    class SampleAdapterSpanLookup(
        private val adapterProvider: () -> ConcatAdapter
    ) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val ordinal = adapterProvider().getItemViewType(position)
            return when (ItemType.fromOrdinal(ordinal)) {
                type<ItemType.Small>() -> 1
                type<ItemType.Large>() -> 2
                type<ItemType.Widest>(),
                type<ItemType.Empty>(),
                type<ItemType.HorizontalRecycler>() -> 3
                else -> throw IllegalStateException(
                    "Unknown viewType = $ordinal for position = $position"
                )
            }
        }
    }
}
