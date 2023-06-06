package com.inlurker.komiq.model.mangadexAPI

import com.inlurker.komiq.model.data.Relationship
import com.inlurker.komiq.model.data.Tag

data class MangaJson(
    val data: MangaJsonData
)

data class MangaJsonData(
    val id: String,
    val type: String,
    val attributes: MangaAttributesJson,
    val relationships: List<Relationship>,
    val tags: List<Tag>
)

data class MangaAttributesJson(
    val title: Map<String, String>,
    val altTitles: List<Map<String, String>>,
    val description: Map<String, String>,
    val originalLanguage: String,
    val lastVolume: String,
    val lastChapter: String,
    val publicationDemographic: String?,
    val status: String,
    val year: Int,
    val contentRating: String,
    val createdAt: String,
    val updatedAt: String
)