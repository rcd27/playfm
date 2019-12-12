package com.rcd27.playfm.data.discover

import com.rcd27.playfm.api.discover.JSONRecording
import com.rcd27.playfm.common.ViewObject
import io.reactivex.functions.Function
import org.threeten.bp.ZonedDateTime

data class Recording(
    val id: Int,
    val date: ZonedDateTime,
    val likesCount: Int,
    val shortText: String,

    val teaserUrl: String
) : ViewObject {

    companion object {
        fun fromJSON(): Function<JSONRecording, Recording> =
            Function { jsonObject ->
                val stubDate = jsonObject.created_at.substringBefore("+").plus("Z[GMT]")
                Recording(
                    jsonObject.id,
                    ZonedDateTime.parse(stubDate),
                    jsonObject.playCount,
                    jsonObject.title,
                    jsonObject.image.formats.recordings_teaser
                )
            }
    }
}