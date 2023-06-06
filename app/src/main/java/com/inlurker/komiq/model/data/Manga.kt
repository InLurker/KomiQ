package com.inlurker.komiq.model.data

import java.time.LocalDateTime

data class Manga(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: List<Relationship>,
    val tags: List<Tag>,
    val cover: String
)

data class Attributes(
    val title: String,
    val altTitle: String,
    val description: String,
    val originalLanguage: String,
    val publicationDemographic: String?,
    val status: String,
    val year: Int,
    val contentRating: String,
    val addedAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class Tag(
    val name: String,
    val group: String
)

data class Relationship(
    val id: String,
    val type: String,
)