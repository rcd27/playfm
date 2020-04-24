package com.rcd27.playfm.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.R

/** Вспомогательный класс-обёртка над [NavController.OnDestinationChangedListener], нужен
 * в том числе e2e тестах, чтобы убедиться, что отработала навигация.
 */
object AppDestinationChangeListener : NavController.OnDestinationChangedListener {

    private var onDestination: BehaviorRelay<AppDestination> = BehaviorRelay.create()

    // Workaround: `onDestinationChanged` is not called during launch
    init {
        onDestination.accept(FeedScreen)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.dest_feed -> onDestination.accept(FeedScreen)
        }
    }
}