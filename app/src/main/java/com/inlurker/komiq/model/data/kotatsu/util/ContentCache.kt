package com.inlurker.komiq.model.data.kotatsu.util

import kotlinx.coroutines.Deferred
import org.koitharu.kotatsu.parsers.model.Manga
import org.koitharu.kotatsu.parsers.model.MangaPage
import org.koitharu.kotatsu.parsers.model.MangaSource

interface ContentCache {

    val isCachingEnabled: Boolean

    suspend fun getDetails(source: MangaSource, url: String): Manga?

    fun putDetails(source: MangaSource, url: String, details: SafeDeferred<Manga>)

    suspend fun getPages(source: MangaSource, url: String): List<MangaPage>?

    fun putPages(source: MangaSource, url: String, pages: SafeDeferred<List<MangaPage>>)

    fun putRelatedManga(source: MangaSource, url: String, related: SafeDeferred<List<Manga>>)

    data class Key(
        val source: MangaSource,
        val url: String,
    )
}

class SafeDeferred<T>(
    private val delegate: Deferred<Result<T>>,
) {

    suspend fun await(): T {
        return delegate.await().getOrThrow()
    }

    suspend fun awaitOrNull(): T? {
        return delegate.await().getOrNull()
    }

    fun cancel() {
        delegate.cancel()
    }
}

