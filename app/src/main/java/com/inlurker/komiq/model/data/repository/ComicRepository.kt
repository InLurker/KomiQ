package com.inlurker.komiq.model.data.repository

import com.inlurker.komiq.model.data.Chapter
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.mangadexapi.adapters.MangaChapterListResponse
import com.inlurker.komiq.model.mangadexapi.adapters.MangadexMangaListResponse
import com.inlurker.komiq.model.mangadexapi.adapters.MangadexMangaResponse
import com.inlurker.komiq.model.mangadexapi.builders.ComicSearchQuery
import com.inlurker.komiq.model.mangadexapi.helper.performMangadexApiRequest
import com.inlurker.komiq.model.mangadexapi.parsers.mangaListResponseToComicList
import com.inlurker.komiq.model.mangadexapi.parsers.mangadexChapterAdapterToChapter
import com.inlurker.komiq.model.mangadexapi.parsers.mangadexDataAdapterToManga
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request


object ComicRepository {
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
}