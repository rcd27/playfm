package com.rcd27.playfm.domain.post

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.data.post.Post
import com.rcd27.playfm.data.post.PostRepository
import javax.inject.Inject

/** Стэйт машина для экрана поста. Не хранит состояний.. */
class PostStateMachine @Inject constructor(private val postRepository: PostRepository) {
    // FIXME: всё заваривание новых объектов должжно быть в DI
    val state: BehaviorRelay<PostState> = BehaviorRelay.create()

    @SuppressLint("CheckResult") // FIXME
    fun refresh(postId: Long) {
        postRepository.getPostById(postId)
            .subscribe({ success ->
                state.accept(PostLoaded(success))
            }, { error ->
                state.accept(PostLoadFailed(error))
            })
    }
}

sealed class PostAction

sealed class PostState
object PostLoading : PostState()
data class PostLoaded(val post: Post) : PostState()
data class PostLoadFailed(val error: Throwable) : PostState()