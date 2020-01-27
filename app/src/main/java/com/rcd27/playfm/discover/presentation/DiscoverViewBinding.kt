package com.rcd27.playfm.discover.presentation

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.R
import com.rcd27.playfm.common.InnerRecyclerViewItemDecoration
import com.rcd27.playfm.common.RecycleViewAdapter
import com.rcd27.playfm.common.OuterRecyclerViewItemDecoration
import com.rcd27.playfm.common.ViewObject
import com.rcd27.playfm.discover.data.DiscoverItem
import com.rcd27.playfm.discover.data.Recording
import com.rcd27.playfm.discover.domain.*
import com.rcd27.playfm.extensions.exhaustive
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_discover_recording.view.*
import kotlinx.android.synthetic.main.item_discover_slider.view.*
import javax.inject.Inject

/* ViewBinding for Discover feature, all view renderings here. */
class DiscoverViewBinding @Inject constructor(
    private val root: View,
    private val errorDisplay: (String, () -> Unit) -> Unit,
    private val stateListener: BehaviorRelay<DiscoverViewState>,
    private val context: Context // FIXME: move to appropriate class
) {

    private val feedRecyclerView = root.findViewById<RecyclerView>(R.id.feedRecyclerView)

    private val discoverListAdapter = RecycleViewAdapter()
    private val trendingRecordingsAdapter = RecycleViewAdapter()

    private val recording = adapterDelegateLayoutContainer<Recording, ViewObject>(
        R.layout.item_discover_recording
    ) {
        bind {
            Picasso.get()
                // TODO: add placeholder, and placeholder for error
                .load(item.teaserUrl)
                .into(containerView.recordingTeaserImageView)

            containerView.recordingTrackName.text = item.shortText
        }
    }

    private val slider = adapterDelegateLayoutContainer<DiscoverItem, ViewObject>(
        R.layout.item_discover_slider
    ) {
        bind {
            val header = containerView.sliderHeader
            header.text = item.header

            containerView.sliderRecyclerView.apply {
                this.adapter = trendingRecordingsAdapter
                addItemDecoration(InnerRecyclerViewItemDecoration)
            }

            trendingRecordingsAdapter.delegatesManager
                .addDelegate(recording)
        }
    }

    init {
        // FIXME: выяснить, почему не хочет принимать MaterialButton
        discoverListAdapter.delegatesManager
            .addDelegate(slider)

        feedRecyclerView.apply {
            addItemDecoration(OuterRecyclerViewItemDecoration)
            // FIXME: add tag layoutManager in xml to remove this line
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = this@DiscoverViewBinding.discoverListAdapter
        }
    }

    fun render(state: DiscoverState) {
        when (state) {
            is TrendingLoading -> {
                stateListener.accept(DiscoverViewState.Loading)
            }
            is TrendingLoaded -> {
                // FIXME: hardcode
                discoverListAdapter.items = listOf(
                    DiscoverItem(
                        "Trending"
                    )
                )
                discoverListAdapter.notifyDataSetChanged()

                trendingRecordingsAdapter.items = state.trendingRecordings
                trendingRecordingsAdapter.notifyDataSetChanged()

                stateListener.accept(DiscoverViewState.TrendingLoaded)
            }
            is TrendingLoadError -> {
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
    object TrendingLoaded : DiscoverViewState()
    object Error : DiscoverViewState()
}