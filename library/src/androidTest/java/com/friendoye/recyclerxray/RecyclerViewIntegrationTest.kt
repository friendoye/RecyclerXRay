package com.friendoye.recyclerxray

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.friendoye.recyclerxray.utils.*
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.LargeVisible
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Visible
import com.friendoye.recyclerxray.utils.RvIntegrationXRayDebugViewHolder
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecyclerViewIntegrationTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(TestActivity::class.java)

    val currentActivity: TestActivity
        get() = activityTestRule.activity

    lateinit var recyclerXRay: LocalRecyclerXRay

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        XRayInitializer.init(isNoOpMode = false)
        recyclerXRay = LocalRecyclerXRay()
        recyclerXRay.settings = XRaySettings.Builder()
            .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
            .withMinDebugViewSize(context.dip(50))
            .build()
    }

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun wrapDoesNotChangeLayout() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(Visible, Visible, Visible)
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOn() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(Visible, Visible, Visible)
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOff() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(Visible, Visible, Visible)
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            recyclerXRay.hideSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkNotifyDataSetChange() {
        val testAdapter = createTestAdapter(Visible, Visible, Visible)
        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            testAdapter.changeItems(Visible, LargeVisible, Visible)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkDiffUtilsChange() {
        val testAdapter = createTestAdapterWithDiffUtil(Visible, Visible, Visible)
        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            testAdapter.changeItems(Visible, LargeVisible, Visible)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkInvisibleItemsAreVisibleWhenXRayIsOn() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapterWithDiffUtil(Visible,
                IntegrationTestItemType.Ghost(), Visible)
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }
}