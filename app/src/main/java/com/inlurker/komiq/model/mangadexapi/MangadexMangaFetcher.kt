package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.mangadexapi.adapters.MangadexMangaListResponse
import com.inlurker.komiq.model.mangadexapi.adapters.MangadexMangaResponse
import com.inlurker.komiq.model.mangadexapi.builders.ComicSearchQuery
import com.inlurker.komiq.model.mangadexapi.helper.mangaListResponseToComicList
import com.inlurker.komiq.model.mangadexapi.helper.mangadexDataAdapterToManga
import com.inlurker.komiq.model.mangadexapi.helper.performMangadexApiRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Request

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

suspend fun getComicList(comicSearchQuery: ComicSearchQuery): List<Comic> {
    val request = Request.Builder().url(comicSearchQuery.toUrlString()).get().build()

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adapter = moshi.adapter(MangadexMangaListResponse::class.java)

    return performMangadexApiRequest(request, adapter)?.run {
        if (result == "ok") {
            data?.let { mangaListResponseToComicList(it) }
        } else {
            emptyList()
        }
    } ?: emptyList()
}
