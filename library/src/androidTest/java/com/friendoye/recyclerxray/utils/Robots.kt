package com.friendoye.recyclerxray.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.friendoye.recyclerxray.test.R

class RecyclingTestRobot {

    fun clickOnItem(position: Int) {
        onView(ViewMatchers.withId(R.id.test_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    ViewActions.click()
                )
            )
    }

    fun scrollToItem(position: Int) {
        onView(ViewMatchers.withId(R.id.test_recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    position
                )
            )
    }
}

inline fun recyclingTestScreen(block: RecyclingTestRobot.() -> Unit) {
    val robot = RecyclingTestRobot()
    robot.block()
}
