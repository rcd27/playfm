package com.rcd27.playfm

import com.rcd27.playfm.discover.api.DiscoverApi
import com.rcd27.playfm.main.dagger.ActivityComponent
import com.rcd27.playfm.main.dagger.ActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    val discoverApi: DiscoverApi

    fun plus(activityModule: ActivityModule): ActivityComponent
}