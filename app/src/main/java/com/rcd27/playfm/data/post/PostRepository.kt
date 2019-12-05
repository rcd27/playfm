package com.rcd27.playfm.data.post

import com.rcd27.playfm.api.post.PostApi
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(private val postApi: PostApi) {

    /** Получить пост по его Id. */
    fun getPostById(postId: Long): Single<Post> {
        return postApi.getPost("$postId")
            .map { response -> response.post }
            .map<Post>(Post.fromJSON)
    }
}