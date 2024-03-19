package com.inlurker.komiq.model.data.kotatsu.parsers

import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.datamodel.Tag
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import org.koitharu.kotatsu.parsers.model.Manga
import org.koitharu.kotatsu.parsers.model.MangaSource
import java.time.LocalDateTime

fun kotatsuMangaToComic(manga: Manga) = Comic(
    id = manga.id.toString(),
    title = manga.title,
    altTitle = manga.altTitle ?: "No alternative titles",
    description = manga.description ?: "No description available",
    languageSetting = ComicLanguageSetting.Japanese,
    originalLanguage = ComicLanguageSetting.Japanese.isoCode,
    publicationDemographic = if (manga.isNsfw) "nsfw" else "safe",
    status = manga.state?.name?.lowercase() ?: "unknown",
    year = 0,
    contentRating = if (manga.isNsfw) "NSFW" else "Safe",
    addedAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now(),
    authors = manga.author?.let { listOf(it) } ?: emptyList(),
    tags = manga.tags.map {
        Tag(
            name = it.title,
            group = it.key
        )
    },
    cover = manga.coverUrl,
    isInLibrary = false,
    url = manga.url,
    publicUrl = manga.publicUrl
)

fun comicToKotatsuManga(comic: Comic) = Manga(
    id = comic.id.toLong(),
    title = comic.title,
    altTitle = comic.altTitle,
    author = comic.authors.joinToString { ", " },
    state = null,
    url = comic.url!!,
    publicUrl = comic.publicUrl!!,
    rating = 0f,
    isNsfw = false,
    coverUrl = comic.cover,
    tags = emptySet(),
    source = MangaSource.RAWKUMA
)