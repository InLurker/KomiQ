package com.inlurker.komiq.model.data.repository

import android.content.Context
import com.inlurker.komiq.model.data.dao.ComicDao
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPages
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPagesResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangaChapterListResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexMangaListResponse
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexMangaResponse
import com.inlurker.komiq.model.data.mangadexapi.builders.ComicSearchQuery
import com.inlurker.komiq.model.data.mangadexapi.helper.performMangadexApiRequest
import com.inlurker.komiq.model.data.mangadexapi.parsers.mangaListResponseToComicList
import com.inlurker.komiq.model.data.mangadexapi.parsers.mangadexChapterAdapterToChapter
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
import okhttp3.Request


object ComicRepository {
    private lateinit var comicDao: ComicDao
    fun initialize(context: Context) {
        val database = ComicDatabase.getDatabase(context)
        comicDao = database.comicDao()
    }
    suspend fun getComic(comicId: String): Comic? {
        val url = "https://api.mangadex.org/manga/$comicId?includes[]=author&includes[]=artist&includes[]=cover_art"
        val request = Request.Builder().url(url).get().build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexMangaResponse::class.java)

        return performMangadexApiRequest(request, adapter)?.run {
            if (result == "ok") {
                data?.let { mangadexDataAdapterToManga(it) }
            } else {
                null
            }
        }
    }

    suspend fun getComicChapterList(comicId: String): List<Chapter> {
        val url = "https://api.mangadex.org/manga/$comicId/feed?"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangaChapterListResponse::class.java)

        var offset = 0
        val limit = 100
        var total = 0

        val chapterList = mutableListOf<Chapter>()

        do {
            val urlWithQueryParams = url.toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("limit", limit.toString())
                .addQueryParameter("offset", offset.toString())
                .addQueryParameter("order[chapter]=", "desc")
                .addQueryParameter("translatedLanguage[]", "en")
                .addQueryParameter("includes[]", "scanlation_group")
                .build()

            val requestWithQueryParams = request.newBuilder()
                .url(urlWithQueryParams)
                .build()

            val response = performMangadexApiRequest(requestWithQueryParams, adapter)
            response?.let { mangaChaptersResponse ->
                val chapters = mangaChaptersResponse.data
                chapters?.let {
                    val parsedChapters = mangadexChapterAdapterToChapter(chapters)
                    chapterList.addAll(parsedChapters)

                    total = mangaChaptersResponse.total
                    offset += limit
                }
            }
        } while (offset < total)

        return chapterList
    }


    fun getComicList(comicSearchQuery: ComicSearchQuery): Flow<List<Comic>> = flow {
        val request = Request.Builder().url(comicSearchQuery.toUrlString()).get().build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexMangaListResponse::class.java)

        val comicList = performMangadexApiRequest(request, adapter)?.run {
            if (result == "ok") {
                data?.let { mangaListResponseToComicList(it) }
            } else {
                emptyList()
            }
        } ?: emptyList()

        emit(comicList)
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

    suspend fun removeComicFromLibrary(comicId: String): Boolean {
        return try {
            comicDao.deleteComicById(comicId)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isComicInLibrary(comicId: String): Flow<Boolean> {
        return comicDao.getComicById(comicId)
            .catch { emit(null) } // Handle any errors and emit null
            .flowOn(Dispatchers.IO) // Switch to the IO dispatcher for database operations
            .map { comic -> comic != null && comic.id == comicId }
    }

}