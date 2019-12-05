package com.rcd27.playfm.presentation.discover

import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofInt
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateLayoutContainerViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.jakewharton.rxrelay2.BehaviorRelay
import com.rcd27.playfm.R
import com.rcd27.playfm.common.DisplayableItem
import com.rcd27.playfm.common.RecycleViewAdapter
import com.rcd27.playfm.common.RecyclerViewItemDecoration
import com.rcd27.playfm.data.discover.FeedPost
import com.rcd27.playfm.domain.feed.*
import com.rcd27.playfm.extensions.exhaustive
import com.rcd27.playfm.extensions.setRightDrawable
import kotlinx.android.synthetic.main.item_feed_post.view.*
import javax.inject.Inject

/* ViewBinding для экрана постов. Вся логика по отрисовке экрана - здесь. */
class FeedViewBinding @Inject constructor(
    private val root: View,
    private val errorDisplay: (String, () -> Unit) -> Unit,
    private val actionListener: (FeedAction) -> Unit,
    private val stateListener: BehaviorRelay<FeedViewState>,
    private val context: Context // FIXME: move to appropriate class
) {

    private val sortByRateButton = root.findViewById<AppCompatButton>(R.id.feedSortByRateButton)
    private val sortByDateButton = root.findViewById<AppCompatButton>(R.id.feedSortByDateButton)
    private val feedRecyclerView = root.findViewById<RecyclerView>(R.id.feedRecyclerView)

    private val adapter = RecycleViewAdapter()
    private val postItem = adapterDelegateLayoutContainer<FeedPost, DisplayableItem>(
        R.layout.item_feed_post
    ) {
        bind {
            val postItemTextView = containerView.postItemTextView
            postItemTextView.text = item.shortText

            val postItemButton = containerView.postItemButton
            postItemButton.setOnClickListener {
                item.changeState()
                stateListener.accept(FeedViewState.PostItemExpand)
            }

            item.onStateChange { state ->
                handleItemState(state, postItemTextView, postItemButton)
            }

            containerView.setOnClickListener {
                actionListener(FeedClicked(item.id))
            }

            handleItemState(item.state, postItemTextView, postItemButton)
        }
    }

    private fun AdapterDelegateLayoutContainerViewHolder<FeedPost>.handleItemState(
        it: FeedPost.FeedPostState,
        postItemTextView: TextView,
        postItemButton: AppCompatImageButton
    ) {
        when (it) {
            is FeedPost.FeedPostState.Expanded -> {
                val animation: ObjectAnimator = ofInt(postItemTextView, "maxLines", 50)
                animation.setDuration(500).start()

                postItemButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_keyboard_arrow_up_light_green_24dp
                    )
                )
            }
            is FeedPost.FeedPostState.Collapsed -> {
                val animation: ObjectAnimator = ofInt(postItemTextView, "maxLines", 2)
                // FIXME: 0.5 sec "lag" while collapsing, figure out
                animation.setDuration(500).start()

                postItemButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_keyboard_arrow_down_dark_green_24dp
                    )
                )
            }
        }.exhaustive
    }

    init {
        // FIXME: выяснить, почему не хочет принимать MaterialButton
        adapter.delegatesManager
            .addDelegate(postItem)

        feedRecyclerView.apply {
            addItemDecoration(RecyclerViewItemDecoration)
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = this@FeedViewBinding.adapter
        }

        sortByDateButton.setOnClickListener {
            actionListener.invoke(SortByDate)
        }

        sortByRateButton.setOnClickListener {
            actionListener.invoke(SortByRate)
        }
    }

    fun render(state: FeedState) {
        when (state) {
            is FeedsLoading -> {
                stateListener.accept(FeedViewState.Loading)
            }
            is FeedLoaded -> {
                val feeds = state.feeds
                adapter.items = feeds
                adapter.notifyDataSetChanged()
                stateListener.accept(FeedViewState.Loaded)
            }
            is FeedLoadingError -> {
                errorDisplay(state.throwable.message ?: "Ошибка при загрузке ленты постов") {
                    // nothing
                }
                stateListener.accept(FeedViewState.Error)
            }
            is FeedSorted -> {
                adapter.items = state.sortedPosts
                adapter.notifyDataSetChanged()

                val up = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_arrow_drop_up_grey_24dp
                )
                val down = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_arrow_drop_down_grey_24dp
                )

                when (state.sorting) {
                    is NoSorting -> {
                        sortByRateButton.setRightDrawable(null)
                        sortByDateButton.setRightDrawable(null)
                    }
                    is SortingLowRateTop -> {
                        sortByRateButton.setRightDrawable(up)
                        sortByDateButton.setRightDrawable(null)
                    }
                    is SortingTopRateTop -> {
                        sortByRateButton.setRightDrawable(down)
                        sortByDateButton.setRightDrawable(null)
                    }
                    is SortingOldestTop -> {
                        sortByRateButton.setRightDrawable(null)
                        sortByDateButton.setRightDrawable(up)
                    }
                    is SortingNewestTop -> {
                        sortByRateButton.setRightDrawable(null)
                        sortByDateButton.setRightDrawable(down)
                    }
                }.exhaustive
                stateListener.accept(FeedViewState.Invalidate)
            }
            is FeedSortingError -> {
                root.background =
                    ContextCompat.getDrawable(context, R.drawable.ic_error_red_24dp)
            }
        }.exhaustive
    }
}

sealed class FeedViewState {
    object Loading : FeedViewState()
    object Loaded : FeedViewState()
    object Error : FeedViewState()

    object Invalidate : FeedViewState()

    object PostItemExpand : FeedViewState()
}