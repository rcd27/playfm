package com.rcd27.playfm.api.discover

import io.reactivex.Single
import retrofit2.http.GET

interface DiscoverApi {

    /**
     * Get trending recordings
     */
    // TODO: move query args to method
    @GET("recordings/discover/trending?page=1&per_page=12&total_pages=0&sort_by=id&order=desc")
    fun getTrending(): Single<List<JSONRecording>>
}