package com.rcd27.playfm.main.dagger

import androidx.navigation.NavController
import com.rcd27.playfm.navigation.AppDestinationChangeListener
import com.rcd27.playfm.navigation.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityModule(
    private val navController: NavController,
    private val errorDisplay: (errorMessage: String, onDismiss: () -> Unit) -> Unit
) {

    @Provides
    fun errorDisplay(): Function2<@JvmWildcard String, @JvmWildcard Function0<Unit>, Unit> =
        errorDisplay

    @Provides
    fun destinationChangeListener() = AppDestinationChangeListener

    @Provides
    @Singleton
    fun router(): Router = Router(navController, AppDestinationChangeListener)
}