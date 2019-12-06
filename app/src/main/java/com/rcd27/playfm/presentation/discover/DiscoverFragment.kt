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

/** Screen for Discover feature. */
class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    // If removed, Dagger2 doesn't compile any factories
    // TODO: Move to Kotlin delegates.
    @Inject
    @JvmField
    var presenter: DiscoverPresenter? = null

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
                            ?: throw RuntimeException("DiscoverPresenter must be initialized")
                    },
                    viewStateListener = BehaviorRelay.create()
                )
            )
        discoverComponent.inject(this)
    }
}