package com.rcd27.playfm.functional.discover

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.jakewharton.rxrelay2.ReplayRelay
import com.rcd27.playfm.R
import com.rcd27.playfm.functional.StateVerifier
import com.rcd27.playfm.presentation.MainActivity
import com.rcd27.playfm.presentation.discover.FeedFragment
import com.rcd27.playfm.presentation.discover.FeedViewState

class DiscoverRobot(testRule: ActivityScenarioRule<MainActivity>) {

    private val flowStateHistory = ReplayRelay.create<FeedViewState>()
    private val stateVerifier = StateVerifier(flowStateHistory)

    init {
        testRule.scenario.onActivity { activity ->
            // FIXME: T_T, но как ещё достать компонент
            val viewStateListener =
                (activity
                    .supportFragmentManager
                    .fragments[0]
                    .childFragmentManager
                    .fragments[0] as FeedFragment)
                    // Думаю, решится, когда будет запилен DI сервис. Тогда можно будет нужные компоненты брать из него
                    .feedComponent
                    .viewStateListener

            viewStateListener
                .subscribe { viewState -> flowStateHistory.accept(viewState) }
        }
    }

    val assertFeedsLoading
        get() = stateVerifier.assertNextState(FeedViewState.Loading)

    val assertFeedsLoaded
        get() = stateVerifier.assertNextState(FeedViewState.Loaded)

    val assertFeedsLoadingError
        get() = stateVerifier.assertNextState(FeedViewState.Error)

    fun clickExpandButton() {
        // FIXME: надо искать кнопки по заданному contentId или что там
        onView(withId(R.id.postItemButton)).perform(click())
    }

    val assertPostItemExpand
        get() = stateVerifier.assertNextState(FeedViewState.PostItemExpand)

    fun clickSortByDateButton() {
        onView(withId(R.id.feedSortByDateButton)).perform(click())
    }

    val assertViewChanged
        get() = stateVerifier.assertNextState(FeedViewState.Invalidate)

    fun clickSortByRateButton() {
        onView(withId(R.id.feedSortByRateButton)).perform(click())
    }
}