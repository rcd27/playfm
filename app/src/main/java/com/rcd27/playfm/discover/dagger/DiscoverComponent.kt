package com.rcd27.playfm.discover.dagger

import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.discover.presentation.DiscoverFragment
import com.rcd27.playfm.discover.presentation.DiscoverViewState
import dagger.Subcomponent

@Subcomponent(
    modules = [
        DiscoverModule::class
    ]
)
interface DiscoverComponent {

    val viewStateListener: BehaviorRelay<DiscoverViewState>

    fun inject(fragment: DiscoverFragment)
}