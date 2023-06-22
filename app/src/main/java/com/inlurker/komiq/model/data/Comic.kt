package com.inlurker.komiq.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Comic(
    @PrimaryKey val id: String = "id",
    val type: String = "type",
    val authors: List<String> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val cover: String = "cover",
    val isInLibrary: Boolean = false,
    val title: String = "title",
    val altTitle: String = "alt title",
    val description: String = "description",
    val originalLanguage: String = "language",
    val publicationDemographic: String? = null,
    val status: String = "status",
    val year: Int = 0,
    val contentRating: String = "rating",
    val addedAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

@Entity
data class Tag(
    @PrimaryKey val name: String = "tag name",
    val group: String = "tag group"
)