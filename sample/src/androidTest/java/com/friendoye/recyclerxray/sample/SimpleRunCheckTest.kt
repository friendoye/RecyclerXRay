package com.friendoye.recyclerxray.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleRunCheckTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    val currentActivity: MainActivity
        get() = activityTestRule.activity

    @Test
    fun simpleUiCheck() {
        activityTestRule.runOnUiThread {
            currentActivity.isFullDataInAdapter = true
        }

        activityTestRule.runOnUiThread {
            currentActivity.toggleAllSecrets()
        }

        activityTestRule.runOnUiThread {
            currentActivity.binding.sampleRecyclerViewView
                .smoothScrollBy(0, currentActivity.dip(500))
        }

        activityTestRule.runOnUiThread {
            currentActivity.isFullDataInAdapter = false
        }

        activityTestRule.runOnUiThread {
            currentActivity.binding.sampleRecyclerViewView
                .smoothScrollBy(0, -currentActivity.dip(500))
        }

        activityTestRule.runOnUiThread {
            currentActivity.toggleAllSecrets()
        }

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }
}
