package com.rcd27.playfm.main.dagger

import com.rcd27.playfm.discover.dagger.DiscoverComponent
import com.rcd27.playfm.discover.dagger.DiscoverModule
import com.rcd27.playfm.auth.domain.AuthStateMachine
import com.rcd27.playfm.navigation.AppDestinationChangeListener
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    val destinationChangedListener: AppDestinationChangeListener

    val authStateMachine: AuthStateMachine

    fun plus(discoverModule: DiscoverModule): DiscoverComponent
}
