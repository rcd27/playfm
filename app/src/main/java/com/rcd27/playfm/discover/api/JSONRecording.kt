package com.rcd27.playfm.discover.api

data class JSONRecording(
    val audio: String,
    val city: String,
    val city_slug: String,
    val created_at: String,
    val hasAudio: Boolean,
    val id: Int,
    val image: JSONImage,
    val page: JSONPage,
    val playCount: Int,
    val `public`: Boolean,
    val recordingDuration: Int,
    val slug: String,
    val tags: List<JSONTag>,
    val title: String,
    val waveform: String
)

data class JSONImage(
    val formats: JSONFormats
)

data class JSONFormats(
    val large: String,
    val medium_small_thumb: String,
    val medium_thumb: String,
    val newsfeed_subject_teaser: String,
    val page_comment_list: String,
    val page_cover: String,
    val page_cover_big: String,
    val page_cover_small: String,
    val page_dummy_cover: String,
    val page_header: String,
    val page_teaser: String,
    val pages_detail: String,
    val pages_detail_new: String,
    val recordings_teaser: String,
    val small_thumb: String
)

data class JSONPage(
    val city: String,
    val city_slug: String,
    val id: Int,
    val image: JSONImage,
    val tags: List<JSONTag>,
    val title: String,
    val type: String,
    val vanity_url: String
)

data class JSONTag(
    val id: Int,
    val name: String,
    val slug: String
)