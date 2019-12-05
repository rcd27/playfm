package com.rcd27.playfm.dagger.main

import androidx.navigation.NavController
import com.rcd27.playfm.navigation.AppDestinationChangeListener
import com.rcd27.playfm.navigation.Router
import dagger.Module
import dagger.Provides

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

    // FIXME: будет/может создавать новый инстанс при каждом инжекте
    @Provides
    fun router(): Router = Router(navController, AppDestinationChangeListener)
}