package com.rcd27.playfm.dagger

import com.rcd27.playfm.api.discover.DiscoverApi
import com.rcd27.playfm.api.post.PostApi
import com.rcd27.playfm.dagger.main.ActivityComponent
import com.rcd27.playfm.dagger.main.ActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    val discoverApi: DiscoverApi

    val postApi: PostApi

    fun plus(activityModule: ActivityModule): ActivityComponent
}