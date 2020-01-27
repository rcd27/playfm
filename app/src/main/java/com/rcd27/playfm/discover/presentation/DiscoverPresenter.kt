package com.rcd27.playfm.discover.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.rcd27.playfm.discover.domain.DiscoverAction
import com.rcd27.playfm.discover.domain.DiscoverStateMachine
import com.rcd27.playfm.discover.domain.Refresh
import com.rcd27.playfm.extensions.exhaustive
import com.rcd27.playfm.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/** Presenter layer for Discover feature. Handles inputs*/
class DiscoverPresenter @Inject constructor(
    lifecycle: Lifecycle,
    private val viewBinding: DiscoverViewBinding,
    private val stateMachine: DiscoverStateMachine
) : LifecycleObserver {

    private val cd = CompositeDisposable()

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        cd += stateMachine.state
            .observeOn(AndroidSchedulers.mainThread())
            // TODO: handle FeedLoadError
            .subscribe(viewBinding::render)

        stateMachine.input(Refresh)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        cd.clear()
    }

    fun input(it: DiscoverAction) {
        when (it) {
            is Refresh -> stateMachine.input(it)
        }.exhaustive
    }
}