package com.rcd27.playfm.domain.feed

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.data.discover.DiscoverRepository
import com.rcd27.playfm.data.discover.FeedPost
import javax.inject.Inject

/** Стэйт машина для экрана постов. Хранит состояние текущей сортировки. */
class FeedStateMachine @Inject constructor(private val repository: DiscoverRepository) {

    val state: BehaviorRelay<FeedState> = BehaviorRelay.create()

    fun input(action: FeedAction) {
        when (action) {
            is RefreshFeed -> {
                refresh()
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
}

sealed class FeedAction
object RefreshFeed : FeedAction()
data class FeedClicked(val feedPostId: Int) : FeedAction()

sealed class FeedState
object FeedsLoading : FeedState()
data class FeedLoaded(val feeds: List<FeedPost>) : FeedState()
data class FeedLoadingError(val throwable: Throwable) : FeedState()
data class FeedSortingError(val throwable: Throwable) : FeedState()

sealed class SortingState
object NoSorting : SortingState()
object SortingNewestTop : SortingState()
object SortingOldestTop : SortingState()
object SortingTopRateTop : SortingState()
object SortingLowRateTop : SortingState()