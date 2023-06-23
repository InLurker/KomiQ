package com.inlurker.komiq.model.data.mangadexapi.adapters

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterPagesResponse(
    val result: String,
    val baseUrl: String,
    val chapter: ChapterPages
)

@JsonClass(generateAdapter = true)
data class ChapterPages(
    val hash: String,
    val data: List<String>,
)