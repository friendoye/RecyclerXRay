package com.friendoye.recyclerxray

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.test.rule.ActivityTestRule
import com.friendoye.recyclerxray.testing.InternalLog
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Ghost
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Indexed
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.LargeVisible
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.SmallOne
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.SmallTwo
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Visible
import com.friendoye.recyclerxray.utils.RecyclingTestActivity
import com.friendoye.recyclerxray.utils.RvIntegrationXRayDebugViewHolder
import com.friendoye.recyclerxray.utils.compareRecyclerScreenshot
import com.friendoye.recyclerxray.utils.createTestAdapter
import com.friendoye.recyclerxray.utils.dip
import com.friendoye.recyclerxray.utils.testScreen
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class XRayDebugViewClicksTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(RecyclingTestActivity::class.java)

    val currentActivity: RecyclingTestActivity
        get() = activityTestRule.activity

    @Before
    fun setup() {
        XRayInitializer.init(isNoOpMode = false)
        InternalLog.current = InternalLog.TestInternalLog()
    }

    @After
    fun teardown() {
        InternalLog.testLogger.reset()
        XRayInitializer.reset()
    }

    @Test
    fun clickOnDebugView_singleTime_printLinkToLogs() {
        val recyclerXRay = LocalRecyclerXRay()
        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 1)
        }

        Assert.assertEquals(
            "VisibleViewHolder(RvIntegrationTestUtils.kt:102)",
            InternalLog.testLogger.accumulatedLogs
                .filter { it.message.startsWith("VisibleViewHolder") }
                .joinToString(separator = "\n") { it.message }
        )
    }

    @Test
    fun clickOnDebugView_multipleTimes_multiplePrintLinkToLogs() {
        val recyclerXRay = LocalRecyclerXRay()
        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 0)
            clickOnItem(position = 1)
            clickOnItem(position = 2)
            clickOnItem(position = 1)
        }

        Assert.assertEquals(
            """
                |LargeVisibleViewHolder(RvIntegrationTestUtils.kt:112)
                |LargeVisibleViewHolder(RvIntegrationTestUtils.kt:112)
            """.trimMargin(),
            InternalLog.testLogger.accumulatedLogs
                .filter { it.message.startsWith("LargeVisibleViewHolder") }
                .joinToString(separator = "\n") { it.message }
        )

        Assert.assertEquals(
            """
                |VisibleViewHolder(RvIntegrationTestUtils.kt:102)
                |VisibleViewHolder(RvIntegrationTestUtils.kt:102)
            """.trimMargin(),
            InternalLog.testLogger.accumulatedLogs
                .filter { it.message.startsWith("VisibleViewHolder") }
                .joinToString(separator = "\n") { it.message }
            )
    }

    @Test
    fun clickOnEmptyDebugView_singleTime_printLinkToLogs() {
        var wasEmptyViewClicked = false
        val recyclerXRay = LocalRecyclerXRay().apply {
            settings = XRaySettings.Builder()
                .withMinDebugViewSize(currentActivity.dip(100))
                .withDefaultXRayDebugViewHolder(object : DefaultXRayDebugViewHolder() {
                    override fun onEmptyViewClick(debugView: View, result: XRayResult) {
                        wasEmptyViewClicked = true
                    }
                })
                .build()
        }
        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 3)
        }

        Assert.assertEquals(
            "GhostViewHolder(RvIntegrationTestUtils.kt:142)",
            InternalLog.testLogger.accumulatedLogs
                .filter { it.message.startsWith("GhostViewHolder") }
                .joinToString(separator = "\n") { it.message }
        )

        Assert.assertTrue(wasEmptyViewClicked)
    }
}

class XRayDebugViewClicksScreenshotTest : ScreenshotTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(RecyclingTestActivity::class.java)

    val currentActivity: RecyclingTestActivity
        get() = activityTestRule.activity

    @Before
    fun setup() {
        XRayInitializer.init(isNoOpMode = false)
    }

    @After
    fun teardown() {
        XRayInitializer.reset()
    }

    @Test
    fun clickOnDebugViewShowsOriginalHolderView() {
        val recyclerXRay = LocalRecyclerXRay()
        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 0)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun doubleClickOnDebugViewShowsDebugView() {
        val recyclerXRay = LocalRecyclerXRay()
        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            repeat(2) {
                clickOnItem(position = 0)
            }
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun clickOnEmptyDebugViewShowsEmptyDebugView() {
        val recyclerXRay = LocalRecyclerXRay().apply {
            settings = XRaySettings.Builder()
                .withMinDebugViewSize(currentActivity.dip(100))
                .build()
        }

        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 3)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun correctRebind_whenScrollDownAndUp() {
        val recyclerXRay = LocalRecyclerXRay()
        val testAdapter = createTestAdapter(LargeVisible, Visible, LargeVisible, Ghost(), Visible, Visible, LargeVisible)

        assert(testAdapter.items.first() == testAdapter.items.last())

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 0)
            scrollToItem(position = testAdapter.items.lastIndex)
            scrollToItem(position = 0)
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun correctRebind_whenAdapterDataWasChangedWithDiffUtil_regularAdapter() {
        val recyclerXRay = LocalRecyclerXRay().apply {
            settings = XRaySettings.Builder()
                .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
                .build()
        }
        val fullItems = listOf(Indexed(1), Indexed(2), SmallOne, SmallOne, SmallOne, SmallTwo, SmallTwo, SmallOne, SmallOne, SmallTwo)
        val partialItems = listOf(SmallOne, /* SmallOne, SmallOne,*/ SmallTwo, /* SmallTwo, SmallOne,*/ SmallOne/*, SmallTwo*/, Indexed(1), Indexed(2))
        val testAdapter = createTestAdapter(
            *fullItems.toTypedArray(),
            useDiffUtils = true
        )

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 0)
            clickOnItem(position = 2)
            clickOnItem(position = 3)
        }

        activityTestRule.runOnUiThread {
            testAdapter.items = partialItems
        }

        activityTestRule.runOnUiThread {
            testAdapter.items = fullItems
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun correctRebind_whenAdapterDataWasChangedWithDiffUtil_concatAdapter() {
        val recyclerXRay = LocalRecyclerXRay().apply {
            settings = XRaySettings.Builder()
                .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
                .build()
        }
        val fullItems1 = listOf(SmallOne, SmallOne, SmallOne, SmallTwo)
        val partialItems1 = listOf(SmallOne, /* SmallOne, SmallOne,*/ SmallTwo)
        val fullItems2 = listOf(SmallTwo, SmallOne, SmallOne, SmallTwo)
        val partialItems2 = listOf(/* SmallTwo,*/ SmallOne, SmallOne/*, SmallTwo*/)
        val testAdapter1 = createTestAdapter(*fullItems1.toTypedArray(), useDiffUtils = true)
        val testAdapter2 = createTestAdapter(*fullItems2.toTypedArray(), useDiffUtils = true)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(ConcatAdapter(
                ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build(),
                listOf(testAdapter1, testAdapter2)
            ))
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            (0..7).forEach { index ->
                clickOnItem(position = index)
            }
        }

        activityTestRule.runOnUiThread {
            testAdapter1.items = partialItems1
            testAdapter2.items = partialItems2
        }

        activityTestRule.runOnUiThread {
            testAdapter1.items = fullItems1
            testAdapter2.items = fullItems2
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun correctRebind_whenAdapterDataWasChangedWithNotifyDataChange() {
        val recyclerXRay = LocalRecyclerXRay().apply {
            settings = XRaySettings.Builder()
                .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
                .build()
        }
        val fullItems = listOf(SmallOne, SmallOne, SmallOne, SmallTwo, SmallTwo, SmallOne, SmallOne, SmallTwo)
        val partialItems = listOf(SmallOne, /* SmallOne, SmallOne,*/ SmallTwo, /* SmallTwo, SmallOne,*/ SmallOne/*, SmallTwo*/)
        val testAdapter = createTestAdapter(
            *partialItems.toTypedArray(),
            useDiffUtils = false
        )

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = recyclerXRay.wrap(testAdapter)
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 0)
            clickOnItem(position = 1)
            clickOnItem(position = 2)
        }

        activityTestRule.runOnUiThread {
            testAdapter.items = fullItems
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }

    @Test
    fun correctRebind_whenAdapterWasReattachedToRecycler() {
        val recyclerXRay = LocalRecyclerXRay().apply {
            settings = XRaySettings.Builder()
                .withDefaultXRayDebugViewHolder(RvIntegrationXRayDebugViewHolder())
                .build()
        }
        val testAdapter = createTestAdapter(
            SmallOne, SmallOne, SmallOne, SmallTwo, SmallTwo, SmallOne, SmallOne, SmallTwo,
            useDiffUtils = false
        )
        val wrappedAdapter = recyclerXRay.wrap(testAdapter)

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = wrappedAdapter
            recyclerXRay.toggleSecrets()
        }

        testScreen {
            clickOnItem(position = 0)
            clickOnItem(position = 4)
        }

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = createTestAdapter()
        }

        activityTestRule.runOnUiThread {
            currentActivity.testRecycler.adapter = wrappedAdapter
        }

        compareRecyclerScreenshot(currentActivity.testRecycler)
    }
}
