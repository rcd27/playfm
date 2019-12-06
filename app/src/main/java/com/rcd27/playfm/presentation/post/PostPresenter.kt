package com.rcd27.playfm.presentation.post

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.rcd27.playfm.domain.post.PostAction
import com.rcd27.playfm.domain.post.PostLoadFailed
import com.rcd27.playfm.domain.post.PostStateMachine
import com.rcd27.playfm.extensions.plusAssign
import com.rcd27.playfm.navigation.Router
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/** Presenter для экрана поста, обрабатывает ошибку загрузки поста. */
class PostPresenter @Inject constructor(
    lifecycle: Lifecycle,
    private val viewBinding: PostViewBinding,
    private val stateMachine: PostStateMachine,
    private val currentPostId: Long,
    private val router: Router,
    private val errorDisplay: (String, () -> Unit) -> Unit
) : LifecycleObserver {

    private val cd = CompositeDisposable()

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        cd += stateMachine.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                if (state is PostLoadFailed) {
                    errorDisplay.invoke("Ошибка при загрузке поста") {
                        router.goBack()
                    }
                }
                viewBinding.render(state)
            }

        stateMachine.refresh(currentPostId)
    }

    fun input(action: PostAction) {
        TODO("Implement")
    }
}
