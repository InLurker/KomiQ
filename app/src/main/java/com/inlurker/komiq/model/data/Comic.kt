package com.inlurker.komiq.model.data

import androidx.room.Entity
import java.time.LocalDateTime

@Entity
data class Comic(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val authors: List<String>,
    val tags: List<Tag>,
    val cover: String,
    val isInLibrary: Boolean,
)

@Entity
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
    val updatedAt: LocalDateTime?,
    val lastReadChaper: Int
)

@Entity
data class Tag(
    val name: String,
    val group: String
)