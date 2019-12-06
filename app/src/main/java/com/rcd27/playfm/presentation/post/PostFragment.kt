package com.rcd27.playfm.presentation.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rcd27.playfm.R
import com.rcd27.playfm.common.POST_ID
import com.rcd27.playfm.dagger.main.post.PostModule
import com.rcd27.playfm.presentation.MainActivity
import javax.inject.Inject

/** Экран поста. */
@Deprecated("This will be changed to RecordingFragment")
class PostFragment : Fragment(R.layout.fragment_post) {

    @Inject
    @JvmField
    var presenter: PostPresenter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val currentPostId = arguments?.getLong(POST_ID)
            ?: throw RuntimeException("Post ID must be provided to PostFragment")

        val postComponent = (activity as MainActivity)
            .activityComponent
            .plus(
                PostModule(
                    requireView(),
                    lifecycle,
                    actionListener = { action ->
                        presenter?.input(action)
                            ?: throw RuntimeException("PostPresenter must be initialized")
                    },
                    currentPostId = currentPostId
                )
            )

        postComponent.inject(this)
    }
}