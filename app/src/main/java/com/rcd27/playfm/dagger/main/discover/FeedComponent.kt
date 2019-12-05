package com.rcd27.playfm.dagger.main.discover

import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.presentation.discover.FeedFragment
import com.rcd27.playfm.presentation.discover.FeedViewState
import dagger.Subcomponent

@Subcomponent(
    modules = [
        FeedModule::class
    ]
)
interface FeedComponent {

    val viewStateListener: BehaviorRelay<FeedViewState>

    fun inject(fragment: FeedFragment)
}