package com.inlurker.komiq.model.mangadexapi.parsers

import com.inlurker.komiq.model.data.Chapter
import com.inlurker.komiq.model.mangadexapi.adapters.MangadexChapterAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun mangadexChapterAdapterToChapter(mangadexChapters: List<MangadexChapterAdapter>): List<Chapter> {
    return mangadexChapters.map { mangadexChapter ->
        val chapterAttributes = mangadexChapter.attributes
        val relationships = mangadexChapter.relationships
        var scanlationGroup = "unknown" // Fallback value

        relationships.forEach { relationship ->
            if (relationship.type == "scanlation_group" && relationship.attributes != null) {
                scanlationGroup = relationship.attributes.name
            }
        }

        Chapter(
            id = mangadexChapter.id,
            volume = chapterAttributes.volume,
            chapter = chapterAttributes.chapter,
            title = chapterAttributes.title ?: "No title",
            publishAt = LocalDateTime.parse(chapterAttributes.publishAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            pages = chapterAttributes.pages,
            scanlationGroup = scanlationGroup
        )
    }
}