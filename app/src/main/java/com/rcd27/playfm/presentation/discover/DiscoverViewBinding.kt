package com.rcd27.playfm.presentation.discover

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.R
import com.rcd27.playfm.common.DisplayableItem
import com.rcd27.playfm.common.RecycleViewAdapter
import com.rcd27.playfm.common.RecyclerViewItemDecoration
import com.rcd27.playfm.data.discover.DiscoverItem
import com.rcd27.playfm.domain.discover.*
import com.rcd27.playfm.extensions.exhaustive
import kotlinx.android.synthetic.main.item_discover_slider.view.*
import javax.inject.Inject

/* ViewBinding for Discover feature, all view renderings here. */
class DiscoverViewBinding @Inject constructor(
        private val root: View,
        private val errorDisplay: (String, () -> Unit) -> Unit,
        private val actionListener: (DiscoverAction) -> Unit,
        private val stateListener: BehaviorRelay<DiscoverViewState>,
        private val context: Context // FIXME: move to appropriate class
) {

    private val feedRecyclerView = root.findViewById<RecyclerView>(R.id.feedRecyclerView)

    private val adapter = RecycleViewAdapter()
    private val slider = adapterDelegateLayoutContainer<DiscoverItem, DisplayableItem>(
            R.layout.item_discover_slider
    ) {
        bind {
            val header = containerView.sliderHeader
            header.text = item.shortText
        }
    }

    init {
        // FIXME: выяснить, почему не хочет принимать MaterialButton
        adapter.delegatesManager
                .addDelegate(slider)

        feedRecyclerView.apply {
            addItemDecoration(RecyclerViewItemDecoration)
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = this@DiscoverViewBinding.adapter
        }
    }

    fun render(state: FeedState) {
        when (state) {
            is FeedsLoading -> {
                stateListener.accept(DiscoverViewState.Loading)
            }
            is FeedLoaded -> {
                val feeds = state.feeds
                adapter.items = feeds
                adapter.notifyDataSetChanged()
                stateListener.accept(DiscoverViewState.Loaded)
            }
            is FeedLoadingError -> {
                errorDisplay(state.throwable.message ?: TODO("handle this")) {
                    // nothing
                }
                stateListener.accept(DiscoverViewState.Error)
            }
            is FeedSortingError -> {
                root.background =
                        ContextCompat.getDrawable(context, R.drawable.ic_error_red_24dp)
            }
        }.exhaustive
    }
}

sealed class DiscoverViewState {
    object Loading : DiscoverViewState()
    object Loaded : DiscoverViewState()
    object Error : DiscoverViewState()
}