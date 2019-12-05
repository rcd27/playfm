package com.rcd27.playfm.dagger.main.discover

import android.view.View
import androidx.lifecycle.Lifecycle
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.domain.feed.FeedAction
import com.rcd27.playfm.presentation.discover.FeedViewState
import dagger.Module
import dagger.Provides

@Module
class FeedModule(
    private val rootView: View,
    private val lifecycle: Lifecycle,
    private val actionListener: (FeedAction) -> Unit,
    private val viewStateListener: BehaviorRelay<FeedViewState>
) {

    @Provides
    fun root(): View = rootView

    @Provides
    // @JvmWildcard is needed because Dagger Compiler is waiting for [Function1<? super FeedAction, Unit>]
    fun actionListener(): (@JvmWildcard FeedAction) -> Unit = actionListener

    @Provides
    fun lifecycle() = lifecycle

    @Provides
    fun stateListener(): BehaviorRelay<FeedViewState> = viewStateListener
}