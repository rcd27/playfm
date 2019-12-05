package com.rcd27.playfm.data.discover

import com.rcd27.playfm.api.discover.JSONRecording
import com.rcd27.playfm.common.DisplayableItem
import io.reactivex.functions.Function
import org.threeten.bp.ZonedDateTime

/** ViewObject для поста из ленты постов. Хранит состояние Открыт/Закрыт, по умолчанию - закрыт. */
data class FeedPost(
    /** Id поста, берётся из модели API. */
    val id: Int,
    /**
     * Дата поста, переведённая в [org.threeten.bp.Instant]
     * см.: https://www.threeten.org/
     */
    val date: ZonedDateTime,
    /** Количество лайков. */
    val likesCount: Int,
    /** Краткий текст поста. */
    val shortText: String,
    /** Состояние во вью: открыт/закрыт. */
    var state: FeedPostState = FeedPostState.Collapsed
) : DisplayableItem {

    private var expandedConsumer: ((FeedPostState) -> Unit)? = null

    companion object {
        fun fromJSON(): Function<JSONRecording, FeedPost> =
            Function { jsonObject ->
                val stubDate = jsonObject.created_at.substringBefore("+").plus("Z[GMT]")
                FeedPost(
                    jsonObject.id,
                    ZonedDateTime.parse(stubDate),
                    jsonObject.playCount,
                    jsonObject.title
                )
            }
    }

    /**
     * Сменить состояние поста:
     *  - [FeedPostState.Collapsed]
     *  - [FeedPostState.Expanded]
     */
    fun changeState() {
        when (state) {
            is FeedPostState.Collapsed -> expand()
            is FeedPostState.Expanded -> collapse()
        }
    }

    private fun expand() {
        state = FeedPostState.Expanded
        expandedConsumer?.invoke(FeedPostState.Expanded)
    }

    private fun collapse() {
        state = FeedPostState.Collapsed
        expandedConsumer?.invoke(FeedPostState.Collapsed)
    }

    /** Колбэк, срабатывающий при смене поста [FeedPost.changeState]. */
    fun onStateChange(callback: (FeedPostState) -> Unit) {
        this.expandedConsumer = callback
    }

    sealed class FeedPostState {
        object Expanded : FeedPostState()
        object Collapsed : FeedPostState()
    }
}