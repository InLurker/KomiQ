package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.data.Attributes
import com.inlurker.komiq.model.data.Manga
import com.inlurker.komiq.model.data.Relationship
import com.inlurker.komiq.model.data.Tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


@JsonClass(generateAdapter = true)
data class MangadexMangaResponse(
    val result: String,
    val response: String,
    val data: MangadexDataAdapter
)

@JsonClass(generateAdapter = true)
data class MangadexMangaListResponse(
    val result: String,
    val response: String,
    val data: List<MangadexDataAdapter>
)

@JsonClass(generateAdapter = true)
data class MangadexDataAdapter(
    val id: String,
    val type: String,
    val attributes: MangadexAttributesAdapter,
    val relationships: List<Relationship>,
)

@JsonClass(generateAdapter = true)
data class MangadexAttributesAdapter(
    val title: Map<String, String>,
    val altTitles: List<Map<String, String>>,
    val description: Map<String, String>,
    val originalLanguage: String?,
    val publicationDemographic: String?,
    val status: String?,
    val year: Int?,
    val contentRating: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val tags: List<MangadexTagAdapter>
)


@JsonClass(generateAdapter = true)
data class MangadexTagAdapter(
    val attributes: MangadexTagAttributesAdapter,
)

@JsonClass(generateAdapter = true)
data class MangadexTagAttributesAdapter(
    val name: Map<String, String>,
    val group: String
)

fun mangadexDataAdapterToManga(data: MangadexDataAdapter): Manga {
    val attributes = data.attributes

    val title = attributes.title.values.firstOrNull()!!

    // Parse altTitle
    val altTitle = findAltTitle(title, attributes.altTitles)

    // Parse description
    val description = findDescription(attributes.description, attributes.altTitles)
    val tagList = mutableListOf<Tag>()

    attributes.tags.forEach { tagEntry ->
        tagEntry.attributes.name["en"]?.let { name ->
            tagList.add(
                Tag(
                    name = name,
                    group = tagEntry.attributes.group
                )
            )
        }
    }

    return Manga(
        id = data.id,
        type = data.type,
        attributes = Attributes(
            title = attributes.title.values.firstOrNull()!!,
            altTitle = altTitle,
            description = description,
            originalLanguage = attributes.originalLanguage ?: "unknown",
            publicationDemographic = attributes.publicationDemographic,
            status = attributes.status ?: "unknown",
            year = attributes.year ?: 0,
            contentRating = attributes.contentRating ?: "unknown",
            addedAt = LocalDateTime.parse(attributes.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = LocalDateTime.parse(attributes.updatedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        ),
        relationships = data.relationships,
        tags = tagList
    )
}


fun mangaListResponseToMangaList(data: List<MangadexDataAdapter>): List<Manga> {
    val dataAdapterList = data

    val mangaList = mutableListOf<Manga>()

    dataAdapterList.forEach { dataAdapter ->
        mangaList.add(mangadexDataAdapterToManga(dataAdapter))
    }
    return mangaList
}

private fun findAltTitle(title: String, altTitles: List<Map<String, String>>): String {
    val englishAltTitles = altTitles.filter { it.containsKey("en") && it["en"] != title }
    val originalLanguageAltTitle = altTitles.find { it.containsKey("en") && it["en"] == title }
    return if (englishAltTitles.isNotEmpty()) {
        englishAltTitles[0]["en"] ?: title
    } else {
        originalLanguageAltTitle?.get(originalLanguageAltTitle["originalLanguage"]) ?: altTitles[0].values.first()
    }
}

private fun findDescription(description: Map<String, String>, altTitles: List<Map<String, String>>): String {
    val englishDescription = description["en"]
    val originalLanguageDescription = description[description["originalLanguage"]]
    return englishDescription ?: originalLanguageDescription ?: altTitles[0].values.first()
}

fun getTop10PopularManga(): List<Manga> {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.mangadex.org/manga?order[followedCount]=desc&limit=10")
        .get()
        .build()

    val response = client.newCall(request).execute()
    val json = response.body?.string()
    print(json)
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adapter = moshi.adapter(MangadexMangaListResponse::class.java)

    var parsedMangaList : List<Manga> = emptyList()
    if(json != null) {
        val mangaListResponse = adapter.fromJson(json)
        if(mangaListResponse != null) {
            parsedMangaList = mangaListResponseToMangaList(mangaListResponse.data)
        }
    }
    return parsedMangaList
}

fun main() {
    val top10PopularManga = getTop10PopularManga()
    for (manga in top10PopularManga) {
        println(manga.tags)
    }
}