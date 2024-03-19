package com.inlurker.komiq.model.data.kotatsu.parsers

import com.inlurker.komiq.model.data.datamodel.Chapter
import org.koitharu.kotatsu.parsers.model.MangaChapter
import org.koitharu.kotatsu.parsers.model.MangaSource
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun kotatsuMangaChapterToChapter(mangaChapter: MangaChapter) = Chapter(
    id = mangaChapter.id.toString(),
    volume = mangaChapter.volume.toFloat(),
    chapter = mangaChapter.number,
    title = "",
    publishAt = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(mangaChapter.uploadDate),
        ZoneId.systemDefault()
    ),
    pages = 0,
    scanlationGroup = mangaChapter.scanlator ?: "RawKuma",
    url = mangaChapter.url
)

fun kotatsuMangaChapterListToChapterList(mangaChapter: List<MangaChapter>): List<Chapter> {
    return mangaChapter.map {
        kotatsuMangaChapterToChapter(it)
    }
}

fun chapterToKotatsuMangaChapter(chapter: Chapter) = MangaChapter(
    id = chapter.id.toLong(),
    name = chapter.title,
    volume = chapter.volume?.toInt() ?: 0,
    number = chapter.chapter,
    url = chapter.url!!,
    scanlator = chapter.scanlationGroup,
    uploadDate = chapter.publishAt.toEpochSecond(ZoneOffset.UTC),
    branch = null,
    source = MangaSource.RAWKUMA
)