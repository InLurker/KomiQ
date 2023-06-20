package com.inlurker.komiq.model.data

import androidx.room.Entity
import java.time.LocalDateTime

@Entity
data class Comic(
    val id: String,
    val type: String,
    val authors: List<String>,
    val tags: List<Tag>,
    val cover: String,
    val isInLibrary: Boolean,
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


@Entity
data class Tag(
    val name: String,
    val group: String
)