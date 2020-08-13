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

    @Before
    fun setup() {
        ActivityTestRule<TestActivity>(TestActivity::class.java)
        val context = InstrumentationRegistry.getInstrumentation().context
        XRayInitializer.init(isNoOpMode = false)
        RecyclerXRay.settings = XRaySettings.Builder()
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
            currentActivity.testRecycler.adapter = RecyclerXRay.wrap(testAdapter)
        }

        compareScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOn() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(Visible, Visible, Visible)
            currentActivity.testRecycler.adapter = RecyclerXRay.wrap(testAdapter)
            RecyclerXRay.showSecrets()
        }

        compareScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOff() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(Visible, Visible, Visible)
            currentActivity.testRecycler.adapter = RecyclerXRay.wrap(testAdapter)
            RecyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            RecyclerXRay.hideSecrets()
        }

        compareScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkNotifyDataSetChange() {
        val testAdapter = createTestAdapter(Visible, Visible, Visible)
        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = RecyclerXRay.wrap(testAdapter)
            RecyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            testAdapter.changeItems(Visible, LargeVisible, Visible)
        }

        compareScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkDiffUtilsChange() {
        val testAdapter = createTestAdapterWithDiffUtil(Visible, Visible, Visible)
        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = RecyclerXRay.wrap(testAdapter)
            RecyclerXRay.showSecrets()
        }

        activityTestRule.runOnUiThread {
            testAdapter.changeItems(Visible, LargeVisible, Visible)
        }

        compareScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkInvisibleItemsAreVisibleWhenXRayIsOn() {
        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapterWithDiffUtil(Visible,
                IntegrationTestItemType.Ghost(), Visible)
            currentActivity.testRecycler.adapter = RecyclerXRay.wrap(testAdapter)
            RecyclerXRay.showSecrets()
        }

        compareScreenshot(currentActivity.testRecycler)
    }
}