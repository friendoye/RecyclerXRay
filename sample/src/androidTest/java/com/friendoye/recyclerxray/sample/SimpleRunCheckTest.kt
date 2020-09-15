package com.friendoye.recyclerxray.sample

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleRunCheckTest {

    @get:Rule
    var activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun simpleUiCheck() {
        activityTestRule.scenario.apply {
            moveToState(Lifecycle.State.RESUMED)
            onActivity { activity ->
                activity.isFullDataInAdapter = true
            }

            onActivity { activity ->
                activity.toggleAllSecrets()
            }

            onActivity { activity ->
                activity.binding.sampleRecyclerViewView
                    .smoothScrollBy(0, activity.dip(500))
            }

            onActivity { activity ->
                activity.isFullDataInAdapter = false
            }

            onActivity { activity ->
                activity.binding.sampleRecyclerViewView
                    .smoothScrollBy(0, -activity.dip(500))
            }

            onActivity { activity ->
                activity.toggleAllSecrets()
            }
        }

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }
}
