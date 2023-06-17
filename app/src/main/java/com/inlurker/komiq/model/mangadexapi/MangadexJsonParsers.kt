package com.inlurker.komiq.model.mangadexapi


import com.inlurker.komiq.model.data.Attributes
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.data.Tag
import com.inlurker.komiq.model.mangadexapi.builder.ComicSearchQuery
import com.inlurker.komiq.model.mangadexapi.constants.GenreTag
import com.inlurker.komiq.model.mangadexapi.constants.ThemeTag
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



fun mangadexDataAdapterToManga(data: MangadexDataAdapter): Comic {
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

    val authorList = mutableListOf<String>()
    var coverFileName = String()
    data.relationships.forEach { relationship ->
        if (relationship.type == "cover_art") {
            relationship.attributes?.let {
                coverFileName = it.fileName?:""
            }
        } else if (relationship.type == "author" || relationship.type == "artist")  {
            relationship.attributes?.let {
                if(it.name != null && !authorList.contains(it.name)) {
                    authorList.add(it.name)
                }
            }
        }
    }

    return Comic(
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
            updatedAt = LocalDateTime.parse(attributes.updatedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            lastReadChaper = 0
        ),
        authors = authorList,
        tags = tagList,
        cover = coverFileName,
        isInLibrary = false
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

fun mangaListResponseToComicList(dataAdapterList: List<MangadexDataAdapter>): List<Comic> {
    val comicList = mutableListOf<Comic>()

    dataAdapterList.forEach { dataAdapter ->
        comicList.add(mangadexDataAdapterToManga(dataAdapter))
    }
    return comicList
}



suspend fun main() {
    val query = ComicSearchQuery.Builder()
        .searchQuery("Mato seihei no")
        .sortingMethod("followedCount")
        .sortingOrder("desc")
        .comicAmount(10)
        .offsetAmount(10)
        .includedTags(listOf(GenreTag.COMEDY))
        .excludedTags(listOf(ThemeTag.MONSTERS))
        .build()

    val urlString = query.toUrlString()
    println(urlString)

    val comicList = getComicList(
        query
    )

    for (comic in comicList) {
        println(comic.attributes.title)
        println(comic.tags)
    }
}
