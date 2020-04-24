package com.rcd27.playfm.discover.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DiscoverApi {

    /**
     * Get trending recordings
     */
    @GET("recordings/discover/trending")
    fun getTrending(@QueryMap options: Map<String, String>): Single<List<JSONRecording>>
}