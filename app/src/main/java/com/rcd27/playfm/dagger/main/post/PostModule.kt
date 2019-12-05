package com.rcd27.playfm.dagger.main.post

import android.view.View
import androidx.lifecycle.Lifecycle
import com.rcd27.playfm.domain.post.PostAction
import dagger.Module
import dagger.Provides

@Module
class PostModule(
    private val root: View,
    private val lifecycle: Lifecycle,
    private val actionListener: (PostAction) -> Unit,
    private val currentPostId: Long
) {

    @Provides
    fun postLifecycle(): Lifecycle = lifecycle

    @Provides
    fun postView(): View = root

    @Provides
    fun postActionListener(): (@JvmWildcard PostAction) -> Unit = actionListener

    @Provides
    fun currentPostId(): Long = currentPostId
}
