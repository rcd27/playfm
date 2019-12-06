package com.rcd27.playfm.presentation.discover

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.rcd27.playfm.domain.discover.DiscoverAction
import com.rcd27.playfm.domain.discover.DiscoverStateMachine
import com.rcd27.playfm.domain.discover.Refresh
import com.rcd27.playfm.extensions.exhaustive
import com.rcd27.playfm.extensions.plusAssign
import com.rcd27.playfm.navigation.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/** Presenter layer for Discover feature. Handles inputs*/
class DiscoverPresenter @Inject constructor(
    lifecycle: Lifecycle,
    private val viewBinding: DiscoverViewBinding,
    private val stateMachine: DiscoverStateMachine,
    private val router: Router
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