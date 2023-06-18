package com.inlurker.komiq.model.mangadexapi.adapters

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MangadexDataAdapter(
    val id: String,
    val type: String,
    val attributes: MangadexMangaAttributesAdapter,
    val relationships: List<MangadexRelationshipAdapter>,
)

@JsonClass(generateAdapter = true)
data class MangadexMangaAttributesAdapter(
    val title: Map<String, String>,
    val altTitles: List<Map<String, String>>,
    val description: Map<String, String>,
    val originalLanguage: String?,
    val publicationDemographic: String?,
    val status: String?,
    val year: Int?,
    val contentRating: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val tags: List<MangadexTagAdapter>
)


@JsonClass(generateAdapter = true)
data class MangadexTagAdapter(
    val attributes: MangadexTagAttributesAdapter,
)

@JsonClass(generateAdapter = true)
data class MangadexTagAttributesAdapter(
    val name: Map<String, String>,
    val group: String
)

@JsonClass(generateAdapter = true)
data class MangadexRelationshipAdapter(
    val id: String,
    val type: String,
    val attributes: MangadexRelationshipAttributesAdapter?
)

@JsonClass(generateAdapter = true)
data class MangadexRelationshipAttributesAdapter(
    val name: String?,
    val fileName: String?
)

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