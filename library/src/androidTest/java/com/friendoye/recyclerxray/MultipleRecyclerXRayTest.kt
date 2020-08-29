package com.friendoye.recyclerxray

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.friendoye.recyclerxray.testing.ExceptionShooter
import com.friendoye.recyclerxray.utils.IntegrationTestItemType.Visible
import com.friendoye.recyclerxray.utils.TestActivity
import com.friendoye.recyclerxray.utils.createTestAdapter
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultipleRecyclerXRayTest {

    companion object {
        @AfterClass
        @JvmStatic
        fun finally() {
            ExceptionShooter.current = ExceptionShooter.RealExceptionShooter
        }
    }

    @get:Rule
    var activityTestRule = ActivityTestRule(TestActivity::class.java)

    val currentActivity: TestActivity
        get() = activityTestRule.activity

    @Before
    fun setup() {
        ExceptionShooter.current = ExceptionShooter.TestExceptionShooter()
        XRayInitializer.init(isNoOpMode = false)
    }

    @After
    fun teardown() {
        ExceptionShooter.testShooter.reset()
        XRayInitializer.reset()
    }

    @Test
    fun multipleRecyclerXRayWasAttached_throwException() {
        val firstXRay = LocalRecyclerXRay().apply {
            settings = settings.copy(label = "firstXRay")
        }
        val secondXRay = LocalRecyclerXRay().apply {
            settings = settings.copy(label = "secondXRay")
        }

        activityTestRule.runOnUiThread {
            val testAdapter = createTestAdapter(Visible, Visible, Visible)
            currentActivity.testRecycler.adapter = firstXRay.wrap(secondXRay.wrap(testAdapter))
        }

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        Assert.assertTrue(ExceptionShooter.testShooter.accumulatedExceptions
            .first() is MultipleRecyclerXRayAttachedException)
    }
}
