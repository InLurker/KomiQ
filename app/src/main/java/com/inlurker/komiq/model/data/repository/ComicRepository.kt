package com.inlurker.komiq.model.data.repository

import android.content.Context
import com.inlurker.komiq.model.data.dao.ComicDao
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.kotatsu.util.InMemoryCookieJar
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPages
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPagesResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexChapterListResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexChapterResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexMangaListResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexMangaResponse
import com.inlurker.komiq.model.data.mangadexapi.builders.ComicSearchQuery
import com.inlurker.komiq.model.data.mangadexapi.helper.performMangadexApiRequest
import com.inlurker.komiq.model.data.mangadexapi.parsers.chapterAdapterListToChapterList
import com.inlurker.komiq.model.data.mangadexapi.parsers.chapterAdapterToChapter
import com.inlurker.komiq.model.data.mangadexapi.parsers.mangaAdapterListToComicList
import com.inlurker.komiq.model.data.mangadexapi.parsers.mangadexDataAdapterToManga
import com.inlurker.komiq.model.data.room.ComicDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koitharu.kotatsu.parsers.KotatsuMangaLoaderContext


object ComicRepository {
    private lateinit var comicDao: ComicDao
    lateinit var currentComic: Comic
    lateinit var currentChapter: Chapter

    val okHttpClient = OkHttpClient()
    val memoryCookieJar = InMemoryCookieJar()

    fun initialize(context: Context) {
        val database = ComicDatabase.getDatabase(context)
        comicDao = database.comicDao()
    }
    suspend fun getComic(comicId: String, languageSetting: ComicLanguageSetting): Comic? {
        val url = "https://api.mangadex.org/manga/$comicId?includes[]=author&includes[]=artist&includes[]=cover_art"
        val request = Request.Builder().url(url).get().build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexMangaResponse::class.java)

        return performMangadexApiRequest(request, adapter)?.run {
            if (result == "ok") {
                data?.let { mangadexDataAdapterToManga(it, languageSetting) }
            } else {
                null
            }
        }
    }

    fun getComicList(comicSearchQuery: ComicSearchQuery, languageSetting: ComicLanguageSetting): Flow<List<Comic>> = flow {
        val request = Request.Builder().url(comicSearchQuery.toUrlString()).get().build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexMangaListResponse::class.java)

        val comicList = performMangadexApiRequest(request, adapter)?.run {
            if (result == "ok") {
                data?.let { mangaAdapterListToComicList(it, languageSetting) }
            } else {
                emptyList()
            }
        } ?: emptyList()

        emit(comicList)
    }

    suspend fun getComicChapterList(comicId: String, languageSetting: ComicLanguageSetting): List<Chapter> {
        val url = "https://api.mangadex.org/manga/$comicId/feed?"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexChapterListResponse::class.java)

        var offset = 0
        val limit = 100
        var total = 0

        val chapterList = mutableListOf<Chapter>()

        do {
            val urlWithQueryParams = url.toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("limit", limit.toString())
                .addQueryParameter("offset", offset.toString())
                .addQueryParameter("order[chapter]", "desc")
                .addQueryParameter("translatedLanguage[]", languageSetting.isoCode)
                .addQueryParameter("includes[]", "scanlation_group")
                .build()

            val requestWithQueryParams = request.newBuilder()
                .url(urlWithQueryParams)
                .build()

            val response = performMangadexApiRequest(requestWithQueryParams, adapter)
            response?.let { mangaChaptersResponse ->
                val chapters = mangaChaptersResponse.data
                chapters?.let {
                    val parsedChapters = chapterAdapterListToChapterList(chapters)
                    chapterList.addAll(parsedChapters)

                    total = mangaChaptersResponse.total
                    offset += limit
                }
            }
        } while (offset < total)

        return chapterList
    }

    suspend fun getChapter(chapterId: String): Chapter? {
        val url = "https://api.mangadex.org/chapter/$chapterId?includes[]=scanlation_group"
        val request = Request.Builder().url(url).get().build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexChapterResponse::class.java)

        return performMangadexApiRequest(request, adapter)?.run {
            if (result == "ok") {
                data?.let { chapterAdapterToChapter(it) }
            } else {
                null
            }
        }
    }

    suspend fun getChapterPages(chapterId: String): ChapterPages? {
        val request = Request.Builder()
            .url("https://api.mangadex.org/at-home/server/$chapterId")
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(ChapterPagesResponse::class.java)

        val response = performMangadexApiRequest(request, jsonAdapter)
        return response?.takeIf { it.result == "ok" }?.chapter
    }

    fun getComicLibrary(): Flow<List<Comic>> {
        return comicDao.getAllComics()
    }

    suspend fun addComicToLibrary(comic: Comic): Boolean {
        return try {
            comicDao.insertComic(comic)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeComicFromLibrary(comicId: String, languageSetting: ComicLanguageSetting): Boolean {
        return try {
            comicDao.deleteComicById(comicId, languageSetting)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isComicInLibrary(comicId: String, languageSetting: ComicLanguageSetting): Flow<Boolean> {
        return comicDao.getComicById(comicId, languageSetting)
            .catch { emit(null) } // Handle any errors and emit null
            .flowOn(Dispatchers.IO) // Switch to the IO dispatcher for database operations
            .map { comic -> comic != null && comic.id == comicId && comic.languageSetting == languageSetting }
    }

    fun getKotatsuLoaderContext(context: Context): KotatsuMangaLoaderContext {
        return KotatsuMangaLoaderContext(
            okHttpClient,
            memoryCookieJar,
            context
        )
    }
}