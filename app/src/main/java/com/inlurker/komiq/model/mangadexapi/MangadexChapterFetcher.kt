package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.mangadexapi.adapters.Chapter
import com.inlurker.komiq.model.mangadexapi.adapters.MangaChapterListResponse
import com.inlurker.komiq.model.mangadexapi.helper.performMangadexApiRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request


/*
get manga chapter list https://api.mangadex.org/manga/$comicid/feed?translatedLanguage[]=en
example url: https://api.mangadex.org/manga/a3f91d0b-02f5-4a3d-a2d0-f0bde7152370/feed?translatedLanguage[]=en
*/

suspend fun getMangaChapterList(comicId: String): List<Chapter> {
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
            .addQueryParameter("translatedLanguage[]", "en")
            .addQueryParameter("limit", limit.toString())
            .addQueryParameter("offset", offset.toString())
            .build()

        val requestWithQueryParams = request.newBuilder()
            .url(urlWithQueryParams)
            .build()

        val response = performMangadexApiRequest(requestWithQueryParams, adapter)
        response?.let { mangaChaptersResponse ->
            val chapters = mangaChaptersResponse.data
            // Process chapters here or store them in a list
            chapterList.addAll(chapters)
            total = mangaChaptersResponse.total

            offset += limit
        }
    } while (offset < total)

    return chapterList
}



// get chapter pages (filenames) https://api.mangadex.org/at-home/server/$comicId
// example url: https://api.mangadex.org/at-home/server/a1bd9359-c160-4fb5-acfe-3f0423441841



// page image https://uploads.mangadex.org/data/$hashed/$filename
// example url: https://uploads.mangadex.org/data/3303dd03ac8d27452cce3f2a882e94b2/1-f7a76de10d346de7ba01786762ebbedc666b412ad0d4b73baa330a2a392dbcdd.png
//


fun main() {
    val comicId = "a3f91d0b-02f5-4a3d-a2d0-f0bde7152370"
    val chapterList: List<Chapter>
    runBlocking {
        chapterList = getMangaChapterList(comicId)
    }
    for ((index, chapter) in chapterList.withIndex()) {
        println(index)
        println("Vol ${chapter.attributes.volume} Ch. ${chapter.attributes.chapter}")
        println("Chapter ID: ${chapter.id}")
        println("Title: ${chapter.attributes.title}")
        println()
    }
}
