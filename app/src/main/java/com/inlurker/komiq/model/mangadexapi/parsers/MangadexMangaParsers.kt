package com.inlurker.komiq.model.mangadexapi.parsers


import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.datamodel.Tag
import com.inlurker.komiq.model.mangadexapi.adapters.MangadexDataAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun mangadexDataAdapterToManga(data: MangadexDataAdapter): Comic {
    val attributes = data.attributes

    val title = attributes.title.values.firstOrNull() ?: "No Comic Title"

    // Parse altTitle
    val altTitle = findAltTitle(title, attributes.altTitles)

    // Parse description
    val description = findDescription(attributes.description, attributes.altTitles)
    val tagList = mutableListOf<Tag>()

    attributes.tags.forEach { tagEntry ->
        tagEntry.attributes.name.get("en")?.let { name ->
            tagList.add(
                Tag(
                    name = name,
                    group = tagEntry.attributes.group
                )
            )
        }
    }

    val authorList = mutableListOf<String>()
    var coverFileName = ""
    data.relationships.forEach { relationship ->
        when (relationship.type) {
            "cover_art" -> relationship.attributes?.fileName?.let {
                coverFileName = it
            }
            "author", "artist" -> relationship.attributes?.name?.let {
                if (it.isNotEmpty() && !authorList.contains(it)) {
                    authorList.add(it)
                }
            }
        }
    }

    return Comic(
        id = data.id,
        type = data.type,
        title = title,
        altTitle = altTitle,
        description = description,
        originalLanguage = attributes.originalLanguage ?: "unknown",
        publicationDemographic = attributes.publicationDemographic ?: "",
        status = attributes.status ?: "unknown",
        year = attributes.year ?: 0,
        contentRating = attributes.contentRating ?: "unknown",
        addedAt = LocalDateTime.parse(attributes.createdAt ?: "", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = LocalDateTime.parse(attributes.updatedAt ?: "", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        authors = authorList,
        tags = tagList,
        cover = coverFileName,
        isInLibrary = false
    )
}

private fun findAltTitle(title: String, altTitles: List<Map<String, String>>): String {
    if (altTitles.isEmpty()) {
        return "No alternative titles"
    }
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
    val originalLanguage = description["originalLanguage"]
    val originalLanguageDescription = originalLanguage?.let { description[it] }
    return englishDescription ?: originalLanguageDescription ?: altTitles.firstOrNull()?.values?.firstOrNull() ?: "No description available"
}

fun mangaListResponseToComicList(dataAdapterList: List<MangadexDataAdapter>): List<Comic> {
    val comicList = mutableListOf<Comic>()

    dataAdapterList.forEach { dataAdapter ->
        comicList.add(mangadexDataAdapterToManga(dataAdapter))
    }
    return comicList
}