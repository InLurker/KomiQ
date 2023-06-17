package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.mangadexapi.builder.ComicSearchQuery
import com.inlurker.komiq.model.mangadexapi.mangadexapihelper.MangaDexApiHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CompletableDeferred
import okhttp3.Request

suspend fun getComicList(
    comicSearchQuery: ComicSearchQuery
): List<Comic> {
    val helper = MangaDexApiHelper.getInstance()

    val request = Request.Builder()
        .url(comicSearchQuery.toUrlString())
        .get()
        .build()

    val comicListDeferred = CompletableDeferred<List<Comic>>()

    helper.enqueueRequest(request) { response ->
        val json = response?.body?.string()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexMangaListResponse::class.java)

        val parsedComicList = if (json != null) {
            val comicListResponse = adapter.fromJson(json)
            comicListResponse?.let { mangaListResponseToComicList(it.data) } ?: emptyList()
        } else {
            emptyList()
        }
        comicListDeferred.complete(parsedComicList)
    }
    return comicListDeferred.await()
}