package com.rcd27.playfm.domain.discover

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.data.discover.DiscoverItem
import com.rcd27.playfm.data.discover.DiscoverRepository
import javax.inject.Inject

/** State machine that is responsible for managing Discover screen */
class DiscoverStateMachine @Inject constructor(private val repository: DiscoverRepository) {

    val state: BehaviorRelay<FeedState> = BehaviorRelay.create()

    fun input(action: DiscoverAction) {
        when (action) {
            is Refresh -> {
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

sealed class DiscoverAction
object Refresh : DiscoverAction()

sealed class FeedState
object FeedsLoading : FeedState()
data class FeedLoaded(val feeds: List<DiscoverItem>) : FeedState()
data class FeedLoadingError(val throwable: Throwable) : FeedState()
data class FeedSortingError(val throwable: Throwable) : FeedState()