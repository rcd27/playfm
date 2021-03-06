package com.rcd27.playfm.discover

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.jakewharton.rxrelay2.ReplayRelay
import com.rcd27.playfm.StateVerifier
import com.rcd27.playfm.main.MainActivity
import com.rcd27.playfm.discover.presentation.DiscoverFragment
import com.rcd27.playfm.discover.presentation.DiscoverViewState

class DiscoverRobot(testRule: ActivityScenarioRule<MainActivity>) {

    private val flowStateHistory = ReplayRelay.create<DiscoverViewState>()
    private val stateVerifier = StateVerifier(flowStateHistory)

    init {
        testRule.scenario.onActivity { activity ->
            // FIXME: T_T, но как ещё достать компонент
            val viewStateListener =
                (activity
                    .supportFragmentManager
                    .fragments[0]
                    .childFragmentManager
                    .fragments[0] as DiscoverFragment)
                    // Думаю, решится, когда будет запилен DI сервис. Тогда можно будет нужные компоненты брать из него
                    .discoverComponent
                    .viewStateListener

            viewStateListener
                .subscribe { viewState -> flowStateHistory.accept(viewState) }
        }
    }

    val assertFeedsLoading
        get() = stateVerifier.assertNextState(DiscoverViewState.Loading)

    val assertFeedsLoaded
        get() = stateVerifier.assertNextState(DiscoverViewState.TrendingLoaded)
}