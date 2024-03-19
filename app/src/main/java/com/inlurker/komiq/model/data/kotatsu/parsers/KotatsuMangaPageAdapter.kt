package com.inlurker.komiq.model.data.kotatsu.parsers

import org.koitharu.kotatsu.parsers.model.MangaPage

fun kotatsuMangaPageToPagesUrl(mangaPages: List<MangaPage>) = mangaPages.map { mangaPage ->
    mangaPage.url
}