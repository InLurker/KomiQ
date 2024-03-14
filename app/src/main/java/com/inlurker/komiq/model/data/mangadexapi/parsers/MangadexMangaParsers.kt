package com.inlurker.komiq.model.data.mangadexapi.parsers


import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.datamodel.Tag
import com.inlurker.komiq.model.data.mangadexapi.adapters.MangadexDataAdapter
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun mangadexDataAdapterToManga(data: MangadexDataAdapter, languageSetting: ComicLanguageSetting): Comic {
    val attributes = data.attributes

    val title = attributes.title.values.firstOrNull() ?: "No Comic Title"

    // Parse altTitle
    val altTitle = findAltTitle(attributes.altTitles, languageSetting.isoCode, attributes.originalLanguage)

    // Parse description
    val description = findDescription(attributes.description, languageSetting.isoCode, attributes.originalLanguage)
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
        languageSetting = languageSetting,
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

private fun findAltTitle(altTitles: List<Map<String, String>>, localizationISO: String, originalLanguage: String?): String {
    if (altTitles.isEmpty()) {
        return "No alternative titles"
    }

    val localizedAltTitles = altTitles.filter { it.containsKey(localizationISO) }
    if (localizedAltTitles.isNotEmpty()) {
        localizedAltTitles[0][localizationISO]?.let {
            return it
        }
    }

    if (localizationISO != originalLanguage) {
        originalLanguage?.let { orgLanguage ->
            val originalLanguageAltTitle = altTitles.filter { it.containsKey(orgLanguage) }
            if (originalLanguageAltTitle.isNotEmpty()) {
                originalLanguageAltTitle[0][orgLanguage]?.let {
                    return it
                }
            }
        }
    }

    val englishISO = ComicLanguageSetting.English.isoCode
    if (localizationISO != englishISO) {
        val englishAltTitles = altTitles.filter { it.containsKey(englishISO) }
        if (englishAltTitles.isNotEmpty()) {
            englishAltTitles[0][englishISO]?.let {
                return it
            }
        }
    }

    return altTitles[0].values.first()
}

private fun findDescription(description: Map<String, String>, localizationISO: String, originalLanguage: String?): String {
    if (description.isEmpty()) {
        return "No description available"
    }

    description[localizationISO]?.let { localizedDescription ->
        return localizedDescription
    }

    if (localizationISO != originalLanguage) {
        originalLanguage?.let { orgLang ->
            description[orgLang]?.let { originalDescription ->
                return originalDescription
            }
        }
    }

    val englishISO = ComicLanguageSetting.English.isoCode
    if (localizationISO != englishISO) {
        description[englishISO]?.let { englishDescription ->
            return englishDescription
        }
    }

    return description.values.first()
}

fun mangaAdapterListToComicList(mangaAdapterList: List<MangadexDataAdapter>, languageSetting: ComicLanguageSetting): List<Comic> {
    return mangaAdapterList.map { mangadexDataAdapterToManga(it, languageSetting) }
}