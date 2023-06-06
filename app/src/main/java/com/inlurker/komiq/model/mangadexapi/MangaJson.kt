package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.data.Relationship
import com.inlurker.komiq.model.data.Tag

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/*
// Define the data classes with Moshi annotations
@JsonClass(generateAdapter = true)
data class Manga(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: List<Relationship>,
    val tags: List<Tag>
)

@JsonClass(generateAdapter = true)
data class Attributes(
    val title: String,
    @Json(name = "altTitles")
    val altTitles: List<AltTitle>,
    val description: Map<String, String>,
    @Json(name = "originalLanguage")
    val originalLanguage: String,
    val publicationDemographic: String?,
    val status: String,
    val year: Int,
    val contentRating: String,
    @Json(name = "createdAt")
    val addedAt: String,
    @Json(name = "updatedAt")
    val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class AltTitle(val name: Map<String, String>)

@JsonClass(generateAdapter = true)
data class Tag(
    val name: String,
    val group: String
)

@JsonClass(generateAdapter = true)
data class Relationship(
    val id: String,
    val type: String
)

// Parse the JSON using Moshi
fun parseJson(json: String): Manga {
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val adapter = moshi.adapter(Manga::class.java)
    return adapter.fromJson(json) ?: throw IllegalArgumentException("Invalid JSON")
}


*/