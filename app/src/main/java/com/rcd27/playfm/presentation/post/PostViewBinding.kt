package com.rcd27.playfm.presentation.post

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.rcd27.playfm.R
import com.rcd27.playfm.common.DisplayableItem
import com.rcd27.playfm.common.RecycleViewAdapter
import com.rcd27.playfm.common.RecyclerViewItemDecoration
import com.rcd27.playfm.data.post.PostImage
import com.rcd27.playfm.domain.post.PostLoadFailed
import com.rcd27.playfm.domain.post.PostLoaded
import com.rcd27.playfm.domain.post.PostLoading
import com.rcd27.playfm.domain.post.PostState
import com.rcd27.playfm.extensions.exhaustive
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_post_image.view.*
import javax.inject.Inject

/** ViewBinding для экрана поста. Инициализация вью и отображение стэйта - тут. */
class PostViewBinding @Inject constructor(
    root: View
) {
    private val postRecyclerView = root.findViewById<RecyclerView>(R.id.postRecyclerView)

    private val adapter = RecycleViewAdapter()

    private val image = adapterDelegateLayoutContainer<PostImage, DisplayableItem>(
        R.layout.item_post_image
    ) {
        bind {
            val photoView = containerView.postPhotoView

            Picasso.get()
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_image_grey_24dp)
                .error(R.drawable.ic_broken_image_grey_24dp)
                .fit()
                .centerInside()
                .into(photoView)
        }
    }

    init {
        adapter.delegatesManager
            .addDelegate(image)

        postRecyclerView.apply {
            addItemDecoration(RecyclerViewItemDecoration)
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = this@PostViewBinding.adapter
        }
    }

    fun render(it: PostState) {
        when (it) {
            is PostLoading -> { /*TODO add krutilka*/
            }
            is PostLoaded -> {
                // FIXME: mapping in ViewBinding
                // Первый элемент - текст поста, потом картинки
                adapter.items = listOf(it.post).plus(it.post.images.map { url -> PostImage(url) })
                adapter.notifyDataSetChanged()

                // TODO: report ViewState
            }
            is PostLoadFailed -> {
                // TODO: можно как-то подкрасить или ещё что
            }
        }.exhaustive
    }
}