package com.inlurker.komiq.model.data.kotatsu

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaParser
import org.koitharu.kotatsu.parsers.model.MangaSource

fun KotatsuMangaParser(source: MangaSource, loaderContext: MangaLoaderContext): MangaParser {
    return loaderContext.newParserInstance(source)
}
