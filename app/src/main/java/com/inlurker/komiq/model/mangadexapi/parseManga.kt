package com.inlurker.komiq.model.mangadexapi


import com.inlurker.komiq.model.data.Attributes
import com.inlurker.komiq.model.data.Manga
import com.inlurker.komiq.model.data.Relationship
import com.inlurker.komiq.model.data.Tag
import com.inlurker.komiq.model.mangadexapi.mangadexapihelper.MangaDexApiHelper
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CompletableDeferred
import okhttp3.Request
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    val attributes: MangadexMangaAttributesAdapter,
    val relationships: List<MangadexRelationshipAdapter>,
)

@JsonClass(generateAdapter = true)
data class MangadexMangaAttributesAdapter(
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

@JsonClass(generateAdapter = true)
data class MangadexRelationshipAdapter(
    val id: String,
    val type: String,
    val attributes: MangadexRelationshipAttributesAdapter?
)

@JsonClass(generateAdapter = true)
data class MangadexRelationshipAttributesAdapter(
    val fileName: String?
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

    val relationshipList = mutableListOf<Relationship>()
    var coverFileName = String()
    data.relationships.forEach { relationship ->
        if (relationship.type != "cover_art") {
            relationshipList.add(
                Relationship(
                    id = relationship.id,
                    type = relationship.type
                )
            )
        } else {
            relationship.attributes?.let {
                coverFileName = it.fileName?:""
            }
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
        relationships = relationshipList,
        tags = tagList,
        cover = coverFileName
    )
}


fun mangaListResponseToMangaList(dataAdapterList: List<MangadexDataAdapter>): List<Manga> {

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


suspend fun getTop10PopularManga(): List<Manga> {
    val helper = MangaDexApiHelper.getInstance()

    val request = Request.Builder()
        .url("https://api.mangadex.org/manga?order[followedCount]=desc&limit=10&includes[]=cover_art")
        .get()
        .build()

    val mangaListDeferred = CompletableDeferred<List<Manga>>()

    helper.enqueueRequest(request) { response ->
        val json = response?.body?.string()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(MangadexMangaListResponse::class.java)

        val parsedMangaList = if (json != null) {
            val mangaListResponse = adapter.fromJson(json)
            mangaListResponse?.let { mangaListResponseToMangaList(it.data) } ?: emptyList()
        } else {
            emptyList()
        }
        mangaListDeferred.complete(parsedMangaList)
    }
    return mangaListDeferred.await()
}

