package com.friendoye.recyclerxray

import androidx.recyclerview.widget.ConcatAdapter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.friendoye.recyclerxray.utils.*
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.*
import com.friendoye.recyclerxray.utils.RvIntegrationXRayDebugViewHolder
import com.friendoye.recyclerxray.utils.createTestAdapter
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConcatAdapterIntegrationTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(TestActivity::class.java)

    val currentActivity: TestActivity
        get() = activityTestRule.activity

    lateinit var firstXRay: LocalRecyclerXRay
    lateinit var secondXRay: LocalRecyclerXRay

    @Before
    fun setup() {
        ActivityTestRule<TestActivity>(TestActivity::class.java)
        val context = InstrumentationRegistry.getInstrumentation().context
        XRayInitializer.init(isNoOpMode = false)

        val defaultSettings = XRaySettings.Builder()
            .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
            .withMinDebugViewSize(context.dip(50))
            .build()
        firstXRay = LocalRecyclerXRay().apply {
            settings = defaultSettings.copy(label = "firstXRay")
        }
        secondXRay = LocalRecyclerXRay().apply {
            settings = defaultSettings.copy(label = "secondXRay")
        }
    }

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun sharedViewTypes_wrapIndividualAdapter_firstRecyclerXRayToggle() {
        activityTestRule.runOnUiThread {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    firstXRay.wrap(createTestAdapter(LargeVisible, Ghost(), Visible, Visible)),
                    secondXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible))
                )
            )
            currentActivity.testRecycler.adapter = testAdapter
            firstXRay.toggleSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun sharedViewTypes_wrapIndividualAdapter_secondRecyclerXRayToggle() {
        activityTestRule.runOnUiThread {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    firstXRay.wrap(createTestAdapter(LargeVisible, Ghost(), Visible, Visible)),
                    secondXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible))
                )
            )
            currentActivity.testRecycler.adapter = testAdapter
            secondXRay.toggleSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun sharedViewTypes_wrapIndividualAdapter_allRecyclerXRayToggles() {
        activityTestRule.runOnUiThread {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    firstXRay.wrap(createTestAdapter(LargeVisible, Ghost(), Visible, Visible)),
                    secondXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible))
                )
            )
            currentActivity.testRecycler.adapter = testAdapter
            firstXRay.toggleSecrets()
            secondXRay.toggleSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun sharedViewTypes_wrapConcatAdapter_toggle() {
        activityTestRule.runOnUiThread {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    createTestAdapter(LargeVisible, Ghost(), Visible, Visible),
                    createTestAdapter(Visible, Ghost(), Visible, LargeVisible)
                )
            )
            currentActivity.testRecycler.adapter = firstXRay.wrap(testAdapter)
            firstXRay.toggleSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }
}