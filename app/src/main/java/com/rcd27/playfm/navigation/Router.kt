package com.rcd27.playfm.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import com.rcd27.playfm.R
import com.rcd27.playfm.common.POST_ID
import javax.inject.Inject

/** Класс, отвечающий за навигацию в приложении. */
class Router @Inject constructor(
    private val navController: NavController,
    destinationChangedListener: OnDestinationChangedListener
) {

    init {
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    // TODO: запомнить пост, при навигации обратно - мотать к нему
    fun goToPostDetail(postId: Int) {
        navController.navigate(R.id.action_go_to_post_dest,
            Bundle().apply {
                putInt(POST_ID, postId)
            }
        )
    }

    fun goBack() {
        navController.popBackStack()
    }
}