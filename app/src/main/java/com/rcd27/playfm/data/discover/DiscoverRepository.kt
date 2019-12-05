package com.rcd27.playfm.data.discover

import com.rcd27.playfm.api.discover.DiscoverApi
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import javax.inject.Inject

class DiscoverRepository @Inject constructor(private val api: DiscoverApi) {

    // TODO: попробовать запилить через Consumer. Поля в классе - ататат
    private var currentPosts: List<FeedPost> = emptyList()

    /**
     * Получить список постов. Если ранее они не были загружены/изменены, взять от бэк-энда.
     */
    fun getPosts(): Single<List<FeedPost>> {
        return if (currentPosts.isEmpty()) {
            api.getTrending()
                // TODO: figure out if this is truly needed, maybe there is some function for this
                // Unwrap List from Single
                .flatMapObservable { response -> fromIterable(response) }
                // Mapping JSON object to ViewObject here in order to fix API changes only
                // in mapping functions, but whole app.
                .map<FeedPost>(FeedPost.fromJSON())
                .toList()
                .map { posts ->
                    currentPosts = posts
                    posts
                }
        } else {
            Single.just(currentPosts)
        }
    }

    /**
     *  Сортирует ранее полученный список постов по дате: старше -> новее.
     *  Если ранее посты не были получены, [currentPosts] - пуст, получить от бэка и отсортировать.
     */
    fun getSortedByDatePosts(): Single<List<FeedPost>> {
        return if (currentPosts.isNotEmpty()) {
            val sorted = currentPosts.sortedBy { post -> post.date }
            currentPosts = sorted
            Single.just(currentPosts)
        } else {
            getPosts().flatMap { getSortedByDatePosts() }
        }
    }

    /**
     *  Сортировка постов: новее -> старше.
     *  Получить список постов от бэка, если текущий список постов пуст.
     */
    fun getSortedByDescendingDate(): Single<List<FeedPost>> {
        return if (currentPosts.isNotEmpty()) {
            val sorted = currentPosts.sortedByDescending { post -> post.date }
            currentPosts = sorted
            Single.just(currentPosts)
        } else {
            getPosts().flatMap { getSortedByDescendingDate() }
        }
    }

    /**
     * Сортировка постов по рейтингу: ниже -> выше.
     */
    fun getSortedByRatePosts(): Single<List<FeedPost>> {
        return if (currentPosts.isNotEmpty()) {
            val sorted = currentPosts.sortedBy { post -> post.likesCount }
            currentPosts = sorted
            Single.just(currentPosts)
        } else {
            getPosts().flatMap { getSortedByRatePosts() }
        }
    }

    /**
     * Сортировка постов по рейтингу: выше -> ниже.
     */
    fun getSortedByDescendingRatePosts(): Single<List<FeedPost>> {
        return if (currentPosts.isNotEmpty()) {
            val sorted = currentPosts.sortedByDescending { post -> post.likesCount }
            currentPosts = sorted
            Single.just(currentPosts)
        } else {
            getPosts().flatMap { getSortedByDescendingRatePosts() }
        }
    }
}
