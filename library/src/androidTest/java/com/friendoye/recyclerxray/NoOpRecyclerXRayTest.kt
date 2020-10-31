package com.friendoye.recyclerxray

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Ghost
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.LargeVisible
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Visible
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

class NoOpRecyclerXRayTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityScenarioRule(TestActivity::class.java)

    val currentActivity: TestActivity
        get() = activityTestRule.activity

    lateinit var recyclerXRay: LocalRecyclerXRay

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        XRayInitializer.init(isNoOpMode = true)
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
        activityTestRule.scenario.onActivity { activity ->
            val testAdapter = createTestAdapter(Visible, Ghost(), LargeVisible)
            activity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOn() {
        activityTestRule.scenario.onActivity { activity ->
            val testAdapter = createTestAdapter(Visible, Ghost(), LargeVisible)
            activity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.showSecrets()
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOff_viaHideSecrets() {
        activityTestRule.scenario.apply {
            onActivity { activity ->
                val testAdapter = createTestAdapter(Visible, Ghost(), LargeVisible)
                activity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
                recyclerXRay.showSecrets()
            }

            onActivity {
                recyclerXRay.hideSecrets()
            }
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun checkXRayIsOff_viaToggleSecrets() {
        activityTestRule.scenario.apply {
            onActivity { activity ->
                val testAdapter = createTestAdapter(Visible, Ghost(), LargeVisible)
                activity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
                recyclerXRay.showSecrets()
            }

            onActivity {
                recyclerXRay.toggleSecrets()
            }
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }
}
