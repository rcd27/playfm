package com.rcd27.playfm.discover.dagger

import android.view.View
import androidx.lifecycle.Lifecycle
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.discover.domain.DiscoverAction
import com.rcd27.playfm.discover.presentation.DiscoverViewState
import dagger.Module
import dagger.Provides

@Module
class DiscoverModule(
    private val rootView: View,
    private val lifecycle: Lifecycle,
    private val actionListener: (DiscoverAction) -> Unit,
    private val viewStateListener: BehaviorRelay<DiscoverViewState>
) {

    @Provides
    fun root(): View = rootView

    @Provides
    // @JvmWildcard is needed because Dagger Compiler is waiting for [Function1<? super DiscoverAction, Unit>]
    fun actionListener(): (@JvmWildcard DiscoverAction) -> Unit = actionListener

    @Provides
    fun lifecycle() = lifecycle

    @Provides
    fun stateListener(): BehaviorRelay<DiscoverViewState> = viewStateListener
}