package com.friendoye.recyclerxray

import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.friendoye.recyclerxray.utils.*
import com.friendoye.recyclerxray.utils.InnerTestItemType.Box
import com.friendoye.recyclerxray.utils.InnerTestItemType.Circle
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.*
import com.friendoye.recyclerxray.utils.RvIntegrationXRayDebugViewHolder
import com.friendoye.recyclerxray.utils.createTestAdapter
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class NestedRecyclerXRayTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(TestActivity::class.java)

    val currentActivity: TestActivity
        get() = activityTestRule.activity

    lateinit var baseSettingsBuilder: XRaySettings.Builder

    @Before
    fun setup() {
        XRayInitializer.init(isNoOpMode = false)
        baseSettingsBuilder = XRaySettings.Builder()
            .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
            .withMinDebugViewSize(currentActivity.dip(150))
    }

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun settingDisabled_doNoShowInspection() {
        val localRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder
                .enableNestedRecyclersSupport(false)
                .withNestedXRaySettingsProvider(null)
                .build()
        }

        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible
            )
            currentActivity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
            localRecyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun settingsEnabled_noNestedProvider_innerAdapterWrappedManuallyWithSameRecyclerXRay_showInspection() {
        val localRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder
                .enableNestedRecyclersSupport(true)
                .withNestedXRaySettingsProvider(null)
                .build()
        }

        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                innerWrapper = { localRecyclerXRay.wrap(it as RecyclerView.Adapter<RecyclerView.ViewHolder>) }
            )
            currentActivity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
        }

        activityTestRule.runOnUiThread {
            localRecyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun settingsEnabled_noNestedProvider_innerAdapterWrappedManuallyWithAnotherRecyclerXRay_doNoShowInspection() {
        val localRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder
                .enableNestedRecyclersSupport(true)
                .withNestedXRaySettingsProvider(null)
                .build()
        }

        val innerRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder.build()
        }

        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                innerWrapper = { innerRecyclerXRay.wrap(it as RecyclerView.Adapter<RecyclerView.ViewHolder>) }
            )
            currentActivity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
        }

        activityTestRule.runOnUiThread {
            localRecyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun settingsEnabled_hasNestedProvider_showInspection() {
        val localRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder
                .enableNestedRecyclersSupport(true)
                .withNestedXRaySettingsProvider(object : NestedXRaySettingsProvider {
                    override fun provide(
                        nestedAdapter: RecyclerView.Adapter<*>,
                        isDecorated: Boolean
                    ): XRaySettings? {
                        return XRaySettings.Builder().build()
                    }
                })
                .build()
        }

        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible
            )
            currentActivity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
            localRecyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun settingsEnabled_hasNestedProvider_innerAdapterHasChanged_showInspection() {
        val localRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder
                .enableNestedRecyclersSupport(true)
                .withNestedXRaySettingsProvider(object : NestedXRaySettingsProvider {
                    override fun provide(
                        nestedAdapter: RecyclerView.Adapter<*>,
                        isDecorated: Boolean
                    ): XRaySettings? {
                        return XRaySettings.Builder().build()
                    }
                })
                .build()
        }
        lateinit var testAdapter: RvIntegrationTestAdapter

        activityTestRule.runOnUiThread {
            testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = true, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                useDiffUtils = true
            )
            currentActivity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
            localRecyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            testAdapter.items = listOf(
                LargeVisible,
                InnerRecycler(
                    changeAdapter = true, useDiffUtils = true,
                    items = listOf(Circle)
                ),
                LargeVisible,
                Visible
            )
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun settingsEnabled_hasNestedProvider_innerDataHasChanged_showInspection() {
        val localRecyclerXRay = LocalRecyclerXRay().apply {
            settings = baseSettingsBuilder
                .enableNestedRecyclersSupport(true)
                .withNestedXRaySettingsProvider(object : NestedXRaySettingsProvider {
                    override fun provide(
                        nestedAdapter: RecyclerView.Adapter<*>,
                        isDecorated: Boolean
                    ): XRaySettings? {
                        return XRaySettings.Builder().build()
                    }
                })
                .build()
        }
        lateinit var testAdapter: RvIntegrationTestAdapter

        activityTestRule.runOnUiThread {
            testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                useDiffUtils = true
            )
            currentActivity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
            localRecyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            testAdapter.items = listOf(
                LargeVisible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Circle)
                ),
                LargeVisible,
                Visible
            )
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }
}