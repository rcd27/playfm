package com.rcd27.playfm.functional.discover

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rcd27.playfm.presentation.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscoverFlowTest {

    @get: Rule
    var activityRule = activityScenarioRule<MainActivity>()

    @Test
    fun mainTest() {
        val robot = DiscoverRobot(activityRule)
        with(robot) {
            assertFeedsLoading

            assertFeedsLoaded

            /* TODO
            clickExpandButton()
            assertPostItemExpand */

            // Check sorting by date
            clickSortByDateButton()
            assertViewChanged
            clickSortByDateButton()
            assertViewChanged

            // Check sorting by rate
            clickSortByRateButton()
            assertViewChanged
            clickSortByRateButton()
            assertViewChanged
        }
    }
}