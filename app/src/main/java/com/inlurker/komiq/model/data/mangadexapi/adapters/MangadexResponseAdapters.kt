package com.inlurker.komiq.model.data.mangadexapi.adapters

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MangadexMangaResponse(
    val result: String,
    val response: String,
    val data: MangadexDataAdapter?
)

@JsonClass(generateAdapter = true)
data class MangadexMangaListResponse(
    val result: String,
    val response: String,
    val data: List<MangadexDataAdapter>?
)

@JsonClass(generateAdapter = true)
data class MangadexChapterResponse(
    val result: String,
    val response: String,
    val data: MangadexChapterAdapter?
)

@JsonClass(generateAdapter = true)
data class MangadexChapterListResponse(
    val result: String,
    val response: String,
    val data: List<MangadexChapterAdapter>?,
    val limit: Int,
    val offset: Int,
    val total: Int
)