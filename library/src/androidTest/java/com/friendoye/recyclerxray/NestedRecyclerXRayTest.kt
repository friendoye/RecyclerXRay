package com.friendoye.recyclerxray

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.friendoye.recyclerxray.utils.InnerTestItemType.Box
import com.friendoye.recyclerxray.utils.InnerTestItemType.Circle
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.InnerRecycler
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.LargeVisible
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Visible
import com.friendoye.recyclerxray.utils.RvIntegrationTestAdapter
import com.friendoye.recyclerxray.utils.RvIntegrationXRayDebugViewHolder
import com.friendoye.recyclerxray.utils.TestActivity
import com.friendoye.recyclerxray.utils.activity
import com.friendoye.recyclerxray.utils.compareRecyclerScreenshot
import com.friendoye.recyclerxray.utils.createTestAdapter
import com.friendoye.recyclerxray.utils.dip
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("UNCHECKED_CAST")
class NestedRecyclerXRayTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityScenarioRule(TestActivity::class.java)

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

        activityTestRule.scenario.apply {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible
            )

            onActivity { activity ->
                activity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
                localRecyclerXRay.showSecrets()
            }
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

        activityTestRule.scenario.apply {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                innerWrapper = { localRecyclerXRay.wrap(it as RecyclerView.Adapter<RecyclerView.ViewHolder>) }
            )

            onActivity { activity ->
                activity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
            }

            onActivity {
                localRecyclerXRay.showSecrets()
            }
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

        activityTestRule.scenario.apply {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                innerWrapper = { innerRecyclerXRay.wrap(it as RecyclerView.Adapter<RecyclerView.ViewHolder>) }
            )

            onActivity { activity ->
                activity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
            }

            onActivity {
                localRecyclerXRay.showSecrets()
            }
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
                        nestedAdapter: RecyclerView.Adapter<*>
                    ): XRaySettings? {
                        return XRaySettings.Builder().build()
                    }
                })
                .build()
        }

        activityTestRule.scenario.apply {
            val testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible
            )

            onActivity { activity ->
                activity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
                localRecyclerXRay.showSecrets()
            }
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
                        nestedAdapter: RecyclerView.Adapter<*>
                    ): XRaySettings? {
                        return XRaySettings.Builder().build()
                    }
                })
                .build()
        }
        lateinit var testAdapter: RvIntegrationTestAdapter

        activityTestRule.scenario.apply {
            testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = true, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                useDiffUtils = true
            )

            onActivity { activity ->
                activity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
                localRecyclerXRay.showSecrets()
            }

            onActivity {
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
                        nestedAdapter: RecyclerView.Adapter<*>
                    ): XRaySettings? {
                        return XRaySettings.Builder().build()
                    }
                })
                .build()
        }
        lateinit var testAdapter: RvIntegrationTestAdapter

        activityTestRule.scenario.apply {
            moveToState(Lifecycle.State.RESUMED)

            testAdapter = createTestAdapter(
                Visible,
                InnerRecycler(
                    changeAdapter = false, useDiffUtils = true,
                    items = listOf(Box, Circle, Box, Circle)
                ),
                Visible,
                useDiffUtils = true
            )

            onActivity { activity ->
                activity.testRecycler.adapter = localRecyclerXRay.wrap(testAdapter)
                localRecyclerXRay.showSecrets()
            }

            onActivity {
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
        }

        //InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        //Thread.sleep(5_000)

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }
}
