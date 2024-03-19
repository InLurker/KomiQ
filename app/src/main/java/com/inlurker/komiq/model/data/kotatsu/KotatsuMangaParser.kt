package com.inlurker.komiq.model.data.kotatsu

import org.koitharu.kotatsu.parsers.InternalParsersApi
import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaParser
import org.koitharu.kotatsu.parsers.PagedMangaParser
import org.koitharu.kotatsu.parsers.model.MangaSource

@OptIn(InternalParsersApi::class)
fun KotatsuPagedMangaParser(source: MangaSource, loaderContext: MangaLoaderContext): PagedMangaParser {
    return KotatsuMangaParser(source, loaderContext) as PagedMangaParser
}

fun KotatsuMangaParser(source: MangaSource, loaderContext: MangaLoaderContext): MangaParser {
    return loaderContext.newParserInstance(source)
}