package com.rcd27.playfm.presentation.discover

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.rcd27.playfm.domain.feed.*
import com.rcd27.playfm.extensions.exhaustive
import com.rcd27.playfm.extensions.plusAssign
import com.rcd27.playfm.navigation.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/** Presenter для экрана постов. Отвечает за фильтрацию [FeedAction] и [FeedState]. */
class FeedPresenter @Inject constructor(
    lifecycle: Lifecycle,
    private val viewBinding: FeedViewBinding,
    private val stateMachine: FeedStateMachine,
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

        stateMachine.input(RefreshFeed)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        cd.clear()
    }

    fun input(it: FeedAction) {
        when (it) {
            // По клику на пост, отрабатывает навигация
            is FeedClicked -> router.goToPostDetail(it.feedPostId)
            // Сортировка проваливается дальше в стэйт-машину
            is RefreshFeed -> stateMachine.input(it)
        }.exhaustive
    }
}