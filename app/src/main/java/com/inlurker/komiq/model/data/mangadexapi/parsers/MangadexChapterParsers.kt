package com.inlurker.komiq.model.data.mangadexapi.parsers

import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexChapterAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun chapterAdapterToChapter(mangadexChapter: MangadexChapterAdapter): Chapter {
    val chapterAttributes = mangadexChapter.attributes
    val relationships = mangadexChapter.relationships
    var scanlationGroup = "unknown" // Fallback value

    relationships.forEach { relationship ->
        if (relationship.type == "scanlation_group" && relationship.attributes != null) {
            scanlationGroup = relationship.attributes.name
        }
    }

    return Chapter(
        id = mangadexChapter.id,
        volume = chapterAttributes.volume,
        chapter = chapterAttributes.chapter ?: 0f,
        title = chapterAttributes.title ?: "No title",
        publishAt = LocalDateTime.parse(chapterAttributes.publishAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        pages = chapterAttributes.pages,
        scanlationGroup = scanlationGroup,
        url = null
    )
}

fun chapterAdapterListToChapterList(mangadexChapterList: List<MangadexChapterAdapter>): List<Chapter> {
    return mangadexChapterList.map { chapterAdapterToChapter(it) }
}