package com.friendoye.recyclerxray

import androidx.recyclerview.widget.ConcatAdapter
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.friendoye.recyclerxray.testing.ExceptionShooter
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Ghost
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.LargeVisible
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Visible
import com.friendoye.recyclerxray.utils.RvIntegrationXRayDebugViewHolder
import com.friendoye.recyclerxray.utils.TestActivity
import com.friendoye.recyclerxray.utils.activity
import com.friendoye.recyclerxray.utils.compareRecyclerScreenshot
import com.friendoye.recyclerxray.utils.createTestAdapter
import com.friendoye.recyclerxray.utils.dip
import com.friendoye.recyclerxray.utils.ensureAllViewHoldersBind
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConcatAdapterIntegrationTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityScenarioRule(TestActivity::class.java)

    val currentActivity: TestActivity
        get() = activityTestRule.activity

    lateinit var firstXRay: LocalRecyclerXRay
    lateinit var secondXRay: LocalRecyclerXRay

    @Before
    fun setup() {
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
        activityTestRule.scenario.apply {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    firstXRay.wrap(createTestAdapter(LargeVisible, Ghost(), Visible, Visible)),
                    secondXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible)),
                ),
            )

            onActivity { activity ->
                activity.testRecycler.adapter = testAdapter
                firstXRay.toggleSecrets()
            }
        }

        compareRecyclerScreenshot(activityTestRule.activity.testRecycler)
    }

    @Test
    fun sharedViewTypes_wrapIndividualAdapter_secondRecyclerXRayToggle() {
        activityTestRule.scenario.apply {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    firstXRay.wrap(createTestAdapter(LargeVisible, Ghost(), Visible, Visible)),
                    secondXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible)),
                ),
            )

            onActivity { activity ->
                activity.testRecycler.adapter = testAdapter
            }

            // TODO: Find out why we should post toggleSecrets()
            onActivity {
                secondXRay.toggleSecrets()
            }

            ensureAllViewHoldersBind(currentActivity.testRecycler)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun sharedViewTypes_wrapIndividualAdapter_allRecyclerXRayToggles() {
        activityTestRule.scenario.apply {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    firstXRay.wrap(createTestAdapter(LargeVisible, Ghost(), Visible, Visible)),
                    secondXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible)),
                ),
            )

            onActivity { activity ->
                activity.testRecycler.adapter = testAdapter
                firstXRay.toggleSecrets()
                secondXRay.toggleSecrets()
            }
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun sharedViewTypes_wrapConcatAdapter_toggle() {
        activityTestRule.scenario.apply {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    createTestAdapter(LargeVisible, Ghost(), Visible, Visible),
                    createTestAdapter(Visible, Ghost(), Visible, LargeVisible),
                ),
            )

            onActivity { activity ->
                activity.testRecycler.adapter = firstXRay.wrap(testAdapter)
                firstXRay.toggleSecrets()
            }
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun failToAttachAdapter_notAllInnerAdaptersWrapped() {
        val shooter = ExceptionShooter.TestExceptionShooter()

        activityTestRule.scenario.apply {
            val testAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(
                    createTestAdapter(LargeVisible, Ghost(), Visible, Visible),
                    firstXRay.wrap(createTestAdapter(Visible, Ghost(), Visible, LargeVisible)),
                ),
            )

            onActivity { activity ->
                try {
                    activity.testRecycler.adapter = testAdapter
                } catch (e: Throwable) {
                    shooter.fire(e)
                }
            }
        }

        Assert.assertTrue(
            shooter.accumulatedExceptions
                .first() is RecyclerAdapterNotFullyWrappedException,
        )
    }
}
