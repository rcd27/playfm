package com.rcd27.playfm.dagger.main.discover

import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.presentation.discover.FeedFragment
import com.rcd27.playfm.presentation.discover.DiscoverViewState
import dagger.Subcomponent

@Subcomponent(
    modules = [
        DiscoverModule::class
    ]
)
interface DiscoverComponent {

    val viewStateListener: BehaviorRelay<DiscoverViewState>

    fun inject(fragment: FeedFragment)
}