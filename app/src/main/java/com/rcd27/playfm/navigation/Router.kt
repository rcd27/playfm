package com.rcd27.playfm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import javax.inject.Inject

/** Класс, отвечающий за навигацию в приложении. */
class Router @Inject constructor(
    private val navController: NavController,
    destinationChangedListener: OnDestinationChangedListener
) {

    init {
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    fun goBack() {
        navController.popBackStack()
    }
}