package com.inlurker.komiq.model.mangadexapi.adapters

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Chapter(
    val id: String,
    val type: String,
    val attributes: ChapterAttributes
)

@JsonClass(generateAdapter = true)
data class ChapterAttributes(
    val volume: Int?,
    val chapter: Float,
    val title: String,
    val publishAt: String,
    val readableAt: String,
    val createdAt: String,
    val updatedAt: String,
    val pages: Int
)

@JsonClass(generateAdapter = true)
data class MangaChapterListResponse(
    val result: String,
    val response: String,
    val data: List<Chapter>,
    val limit: Int,
    val offset: Int,
    val total: Int
)
