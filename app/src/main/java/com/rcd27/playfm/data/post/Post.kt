package com.rcd27.playfm.data.post

import com.rcd27.playfm.api.post.PostApi
import com.rcd27.playfm.common.DisplayableItem
import io.reactivex.functions.Function

/** ViewObject для экрана поста (детали поста)*/
data class Post(
    /** Id поста, из API. */
    val postId: String,
    /** Заголовок. */
    val title: String,
    /** Полный текст поста. **/
    val text: String,
    /** Список изображений, где [String] - URL, по которому находится изображение. */
    val images: List<String>,
    val likesCount: Long
) : DisplayableItem {
    companion object {
        val fromJSON: Function<PostApi.JSONPostDetails, Post> =
            Function { jsonObject ->
                Post(
                    jsonObject.postId,
                    jsonObject.title,
                    jsonObject.text,
                    jsonObject.images,
                    jsonObject.likes_count
                )
            }
    }
}

data class PostImage(
    val imageUrl: String
) : DisplayableItem