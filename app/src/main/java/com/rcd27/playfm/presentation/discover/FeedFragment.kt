package com.rcd27.playfm.presentation.discover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.R
import com.rcd27.playfm.dagger.main.discover.DiscoverComponent
import com.rcd27.playfm.dagger.main.discover.DiscoverModule
import com.rcd27.playfm.presentation.MainActivity
import javax.inject.Inject
import kotlin.properties.Delegates

/** Экран постов. */
class FeedFragment : Fragment(R.layout.fragment_discover) {

    // Если убрать, то даггер не идёт по графу зависимостей и не создаёт ничего
    // TODO: реализовать через котлиновские делегаты, убрав туда аннотации.
    @Inject
    @JvmField
    var presenter: FeedPresenter? = null

    var discoverComponent: DiscoverComponent by Delegates.notNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        discoverComponent = (activity as MainActivity)
            .activityComponent
            .plus(
                DiscoverModule(
                    requireView(),
                    lifecycle,
                    actionListener = { action ->
                        presenter?.input(action)
                            ?: throw RuntimeException("FeedPresenter must be initialized")
                    },
                    viewStateListener = BehaviorRelay.create()
                )
            )
        discoverComponent.inject(this)
    }
}