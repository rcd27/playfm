package com.rcd27.playfm.data.discover

import com.rcd27.playfm.api.discover.DiscoverApi
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import javax.inject.Inject

class DiscoverRepository @Inject constructor(private val api: DiscoverApi) {

    // TODO: попробовать запилить через Consumer. Поля в классе - ататат
    private var currentPosts: List<Recording> = emptyList()

    /**
     * Get 1 page of 12 trending recordings which are sorted by [com.rcd27.playfm.api.discover.JSONRecording.id]
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
                // TODO: figure out if this is truly needed, maybe there is some function for this
                // Unwrap List from Single
                .flatMapObservable { response -> fromIterable(response) }
                // Mapping JSON object to ViewObject here in order to fix API changes only
                // in mapping functions, but whole app.
                .map<Recording>(Recording.fromJSON())
                .toList()
                .map { posts ->
                    currentPosts = posts
                    posts
                }
        } else {
            Single.just(currentPosts)
        }
    }
}
