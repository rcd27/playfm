package com.rcd27.playfm.data.discover

import com.rcd27.playfm.api.discover.JSONRecording
import com.rcd27.playfm.common.DisplayableItem
import io.reactivex.functions.Function
import org.threeten.bp.ZonedDateTime

data class DiscoverItem(
    val id: Int,
    val date: ZonedDateTime,
    val likesCount: Int,
    val shortText: String
) : DisplayableItem {

    companion object {
        fun fromJSON(): Function<JSONRecording, DiscoverItem> =
            Function { jsonObject ->
                val stubDate = jsonObject.created_at.substringBefore("+").plus("Z[GMT]")
                DiscoverItem(
                    jsonObject.id,
                    ZonedDateTime.parse(stubDate),
                    jsonObject.playCount,
                    jsonObject.title
                )
            }
    }
}