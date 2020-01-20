package com.rcd27.playfm.dagger.main

import com.rcd27.playfm.dagger.main.discover.DiscoverComponent
import com.rcd27.playfm.dagger.main.discover.DiscoverModule
import com.rcd27.playfm.domain.auth.AuthStateMachine
import com.rcd27.playfm.navigation.AppDestinationChangeListener
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    val destinationChangedListener: AppDestinationChangeListener

    val authStateMachine: AuthStateMachine

    fun plus(discoverModule: DiscoverModule): DiscoverComponent
}
