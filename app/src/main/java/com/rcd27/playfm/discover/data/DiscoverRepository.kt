package com.rcd27.playfm.discover.data

import com.rcd27.playfm.discover.api.DiscoverApi
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import javax.inject.Inject

class DiscoverRepository @Inject constructor(private val api: DiscoverApi) {

    private val currentPosts: MutableList<Recording> = mutableListOf()

    /**
     * Get 1 page of 12 trending recordings which are sorted by [com.rcd27.playfm.discover.api.JSONRecording]
     */
    fun getTrendingRecordings(): Single<List<Recording>> {
        return if (currentPosts.isEmpty()) {
            api.getTrending(
                mapOf(
                    "page" to "1",
                    "per_page" to "12",
                    "total_pages" to "0",
                    "sort_by" to "id",
                    "order" to "desc"
                )
            )
                // Unwrap List from Single
                .flatMapObservable { response -> fromIterable(response) }
                // Mapping JSON object to ViewObject here in order to fix API changes only
                // in mapping functions, but whole app.
                .map<Recording>(Recording.fromJSON())
                .toList()
                .map { posts ->
                    currentPosts.addAll(posts)
                    posts
                }
        } else {
            Single.just(currentPosts)
        }
    }
}
