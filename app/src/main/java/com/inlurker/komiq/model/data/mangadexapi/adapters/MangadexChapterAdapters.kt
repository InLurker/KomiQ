package com.inlurker.komiq.model.data.mangadexapi.adapters

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MangadexChapterAdapter(
    val id: String,
    val type: String,
    val attributes: MangadexChapterAttributesAdapter,
    val relationships: List<MangadexChapterRelationshipsAdapter>
)

@JsonClass(generateAdapter = true)
data class MangadexChapterAttributesAdapter(
    val volume: Int?,
    val chapter: Float,
    val title: String?,
    val publishAt: String,
    val pages: Int
)

@JsonClass(generateAdapter = true)
data class MangadexChapterRelationshipsAdapter(
    val type: String,
    val attributes: MangadexChapterRelationshipsAttributesAdapter?
)


@JsonClass(generateAdapter = true)
data class MangadexChapterRelationshipsAttributesAdapter(
    val name: String //this will be scanlation group name
)