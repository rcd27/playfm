package com.rcd27.playfm.domain.discover

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.data.discover.Recording
import com.rcd27.playfm.data.discover.DiscoverRepository
import javax.inject.Inject

/** State machine that is responsible for managing Discover screen */
class DiscoverStateMachine @Inject constructor(private val repository: DiscoverRepository) {

    val state: BehaviorRelay<DiscoverState> = BehaviorRelay.create()

    fun input(action: DiscoverAction) {
        when (action) {
            is Refresh -> {
                refresh()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun refresh() {
        // TODO: provide one Composite Disposable in Presenter layer, which will be managed there
        repository.getTrendingRecordings()
            .doOnSubscribe { state.accept(TrendingLoading) }
            .subscribe({ posts ->
                state.accept(TrendingLoaded(posts))
            }, { error ->
                state.accept(TrendingLoadError(error))
            })
    }
}

sealed class DiscoverAction
object Refresh : DiscoverAction()

sealed class DiscoverState
object TrendingLoading : DiscoverState()
data class TrendingLoaded(val trendingRecordings: List<Recording>) : DiscoverState()
data class TrendingLoadError(val throwable: Throwable) : DiscoverState()
data class FeedSortingError(val throwable: Throwable) : DiscoverState()