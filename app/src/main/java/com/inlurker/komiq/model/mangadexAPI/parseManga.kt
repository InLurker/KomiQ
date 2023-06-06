package com.inlurker.komiq.model.mangadexAPI

import com.google.gson.Gson
import com.inlurker.komiq.model.data.Attributes
import com.inlurker.komiq.model.data.Manga

fun parseManga(json: String): Manga {
    val gson = Gson()
    val mangaJson = gson.fromJson(json, MangaJson::class.java)

    val attributes = mangaJson.data.attributes

    // Parse altTitle
    val altTitle = findAltTitle(attributes.title.values.firstOrNull()!!, attributes.altTitles)

    // Parse description
    val description = findDescription(attributes.description, attributes.altTitles)

    return Manga(
        id = mangaJson.data.id,
        type = mangaJson.data.type,
        attributes = Attributes(
            title = attributes.title.values.firstOrNull()!!,
            altTitle = altTitle,
            description = description,
            originalLanguage = attributes.originalLanguage,
            lastVolume = attributes.lastVolume,
            lastChapter = attributes.lastChapter,
            publicationDemographic = attributes.publicationDemographic,
            status = attributes.status,
            year = attributes.year,
            contentRating = attributes.contentRating,
            addedAt = attributes.createdAt,
            updatedAt = attributes.updatedAt
        ),
        relationships = mangaJson.data.relationships,
        tags = mangaJson.data.tags
    )
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

