package com.inlurker.komiq.model.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import java.time.LocalDateTime

@Entity(primaryKeys = ["id", "languageSetting"])
data class Comic(
    val id: String = "id",
    val authors: List<String> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val cover: String = "cover",
    val isInLibrary: Boolean = false,
    val title: String = "Comic Title",
    val altTitle: String = "Comic Alt title",
    val description: String = "Comic Description",
    val languageSetting: ComicLanguageSetting = ComicLanguageSetting.English,
    val originalLanguage: String = "language",
    val publicationDemographic: String? = null,
    val status: String = "status",
    val year: Int = 0,
    val contentRating: String = "rating",
    val addedAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val url: String? = null,
    val publicUrl: String? = null
)

@Entity
data class Tag(
    @PrimaryKey val name: String = "tag name",
    val group: String = "tag group"
)