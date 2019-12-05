package com.rcd27.playfm.api.post

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApi {

    /**
     * Конкретный пост по {id}.
     */
    @GET("{postId}.json")
    fun getPost(@Path("postId") postId: String): Single<GetPostByIdResponse>

    data class GetPostByIdResponse(val post: JSONPostDetails)

    data class JSONPostDetails(
        val postId: String,
        val timeshamp: String, // FIXME: опечатка в api
        val title: String,
        val text: String,
        val images: List<String>,
        val likes_count: Long
    )
}