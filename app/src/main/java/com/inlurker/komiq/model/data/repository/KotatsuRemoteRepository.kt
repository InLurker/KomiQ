package com.inlurker.komiq.model.data.repository

import androidx.collection.MutableLongSet
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.request.CachePolicy
import com.inlurker.komiq.model.data.kotatsu.KotatsuSourceSettings
import com.inlurker.komiq.model.data.kotatsu.util.ContentCache
import com.inlurker.komiq.model.data.kotatsu.util.SafeDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import org.koitharu.kotatsu.parsers.InternalParsersApi
import org.koitharu.kotatsu.parsers.MangaParser
import org.koitharu.kotatsu.parsers.MangaParserAuthProvider
import org.koitharu.kotatsu.parsers.config.ConfigKey
import org.koitharu.kotatsu.parsers.model.ContentRating
import org.koitharu.kotatsu.parsers.model.Favicons
import org.koitharu.kotatsu.parsers.model.Manga
import org.koitharu.kotatsu.parsers.model.MangaChapter
import org.koitharu.kotatsu.parsers.model.MangaListFilter
import org.koitharu.kotatsu.parsers.model.MangaPage
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.model.MangaState
import org.koitharu.kotatsu.parsers.model.MangaTag
import org.koitharu.kotatsu.parsers.model.SortOrder
import org.koitharu.kotatsu.parsers.util.domain
import org.koitharu.kotatsu.parsers.util.runCatchingCancellable
import java.util.Locale


class KotatsuRemoteRepository(
    private val parser: MangaParser,
    private val cache: ContentCache
) : KotatsuRepository, Interceptor {

    override val source: MangaSource
        get() = parser.source

    override val sortOrders: Set<SortOrder>
        get() = parser.availableSortOrders

    override val states: Set<MangaState>
        get() = parser.availableStates

    override val contentRatings: Set<ContentRating>
        get() = parser.availableContentRating

    override var defaultSortOrder: SortOrder
        get() = getConfig().defaultSortOrder ?: sortOrders.first()
        set(value) {
            getConfig().defaultSortOrder = value
        }

    override val isMultipleTagsSupported: Boolean
        get() = parser.isMultipleTagsSupported

    override val isSearchSupported: Boolean
        get() = parser.isSearchSupported

    override val isTagsExclusionSupported: Boolean
        get() = parser.isTagsExclusionSupported

    @OptIn(InternalParsersApi::class)
    var domain: String
        get() = parser.domain
        set(value) {
            getConfig()[parser.configKeyDomain] = value
        }

    @OptIn(InternalParsersApi::class)
    val domains: Array<out String>
        get() = parser.configKeyDomain.presetValues

    val headers: Headers
        get() = parser.headers

    private fun getConfig() = parser.config as KotatsuSourceSettings

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (parser is Interceptor) {
            parser.intercept(chain)
        } else {
            chain.proceed(chain.request())
        }
    }

    override suspend fun getList(offset: Int, filter: MangaListFilter?): List<Manga> {
        return parser.getList(offset, filter)
    }

    override suspend fun getDetails(manga: Manga): Manga = getDetails(manga, CachePolicy.ENABLED)

    override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
        cache.getPages(source, chapter.url)?.let { return it }
        val pages = asyncSafe {
            parser.getPages(chapter).distinctById()
        }
        cache.putPages(source, chapter.url, pages)
        return pages.await()
    }

    override suspend fun getPageUrl(page: MangaPage): String {
         return parser.getPageUrl(page)
    }

    override suspend fun getTags(): Set<MangaTag> {
        return parser.getAvailableTags()
    }

    override suspend fun getLocales(): Set<Locale> {
        return parser.getAvailableLocales()
    }

    suspend fun getFavicons(): Favicons {
        return parser.getFavicons()
    }

    suspend fun getDetails(manga: Manga, cachePolicy: CachePolicy): Manga {
        if (cachePolicy.readEnabled) {
            cache.getDetails(source, manga.url)?.let { return it }
        }
        val details = asyncSafe {
            parser.getDetails(manga)
        }
        if (cachePolicy.writeEnabled) {
            cache.putDetails(source, manga.url, details)
        }
        return details.await()
    }

    suspend fun peekDetails(manga: Manga): Manga? {
        return cache.getDetails(source, manga.url)
    }

    suspend fun find(manga: Manga): Manga? {
        val list = getList(0, MangaListFilter.Search(manga.title))
        return list.find { x -> x.id == manga.id }
    }

    fun getAuthProvider(): MangaParserAuthProvider? = parser as? MangaParserAuthProvider

    fun getConfigKeys(): List<ConfigKey<*>> = ArrayList<ConfigKey<*>>().also {
        parser.onCreateConfig(it)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun <T> asyncSafe(block: suspend CoroutineScope.() -> T): SafeDeferred<T> {
        var dispatcher = currentCoroutineContext()[CoroutineDispatcher.Key]
        if (dispatcher == null || dispatcher is MainCoroutineDispatcher) {
            dispatcher = Dispatchers.Default
        }
        return SafeDeferred(
            processLifecycleScope.async(dispatcher) {
                runCatchingCancellable { block() }
            }
        )
    }

    private fun List<MangaPage>.distinctById(): List<MangaPage> {
        if (isEmpty()) {
            return emptyList()
        }
        val result = ArrayList<MangaPage>(size)
        val set = MutableLongSet(size)
        for (page in this) {
            if (set.add(page.id)) {
                result.add(page)
            }
        }
        return result
    }

    private fun Result<*>.isValidResult() = isSuccess && (getOrNull() as? Collection<*>)?.isEmpty() != true
}

val processLifecycleScope: LifecycleCoroutineScope
    inline get() = ProcessLifecycleOwner.get().lifecycleScope