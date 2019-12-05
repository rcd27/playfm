package com.rcd27.playfm.domain.feed

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.data.discover.FeedPost
import com.rcd27.playfm.data.discover.DiscoverRepository
import com.rcd27.playfm.extensions.exhaustive
import javax.inject.Inject

/** Стэйт машина для экрана постов. Хранит состояние текущей сортировки. */
class FeedStateMachine @Inject constructor(private val repository: DiscoverRepository) {

    val state: BehaviorRelay<FeedState> = BehaviorRelay.create()

    private var currentSorting: SortingState = NoSorting

    fun input(action: FeedAction) {
        when (action) {
            is RefreshFeed -> {
                refresh()
            }
            is SortByDate -> {
                when (currentSorting) {
                    is
                    SortingOldestTop,
                    NoSorting,
                    SortingTopRateTop,
                    SortingLowRateTop -> sortByNewest()

                    is SortingNewestTop -> sortByOldest()
                }.exhaustive
            }
            is SortByRate -> {
                when (currentSorting) {
                    is
                    SortingLowRateTop,
                    NoSorting,
                    SortingOldestTop,
                    SortingNewestTop -> sortTopRateTop()

                    is SortingTopRateTop -> sortLowRateTop()
                }.exhaustive
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun refresh() {
        // TODO: добавить все чейны в один большой CompositeDisposable для всего приложения
        repository.getPosts()
            .doOnSubscribe { state.accept(FeedsLoading) }
            .subscribe({ posts ->
                state.accept(FeedLoaded(posts))
            }, { error ->
                state.accept(FeedLoadingError(error))
            })
    }

    @SuppressLint("CheckResult")
    private fun sortByOldest() {
        repository.getSortedByDatePosts()
            .subscribe(
                { sortedPosts ->
                    currentSorting = SortingOldestTop
                    state.accept(FeedSorted(sortedPosts, currentSorting))
                },
                { sortingError -> state.accept(FeedSortingError(sortingError)) })
    }

    @SuppressLint("CheckResult")
    private fun sortByNewest() {
        repository.getSortedByDescendingDate()
            .subscribe(
                { sortedPosts ->
                    currentSorting = SortingNewestTop
                    state.accept(FeedSorted(sortedPosts, currentSorting))
                },
                { sortingError -> state.accept(FeedSortingError(sortingError)) })
    }

    @SuppressLint("CheckResult")
    private fun sortLowRateTop() {
        repository.getSortedByRatePosts()
            .subscribe({ sorted ->
                currentSorting = SortingLowRateTop
                state.accept(FeedSorted(sorted, currentSorting))
            }, { error ->
                state.accept(FeedSortingError(error))
            })
    }

    @SuppressLint("CheckResult")
    private fun sortTopRateTop() {
        repository.getSortedByDescendingRatePosts()
            .subscribe({ sorted ->
                currentSorting = SortingTopRateTop
                state.accept(FeedSorted(sorted, currentSorting))
            }, { error ->
                state.accept(FeedSortingError(error))
            })
    }
}

sealed class FeedAction
object RefreshFeed : FeedAction()
data class FeedClicked(val feedPostId: Int) : FeedAction()
object SortByDate : FeedAction()
object SortByRate : FeedAction()

sealed class FeedState
object FeedsLoading : FeedState()
data class FeedLoaded(val feeds: List<FeedPost>) : FeedState()
data class FeedLoadingError(val throwable: Throwable) : FeedState()
data class FeedSorted(val sortedPosts: List<FeedPost>, val sorting: SortingState) : FeedState()
data class FeedSortingError(val throwable: Throwable) : FeedState()

sealed class SortingState
object NoSorting : SortingState()
object SortingNewestTop : SortingState()
object SortingOldestTop : SortingState()
object SortingTopRateTop : SortingState()
object SortingLowRateTop : SortingState()