package com.inlurker.komiq.model.data.mangadexapi.builders

import com.inlurker.komiq.model.data.mangadexapi.constants.MangaOrderOptions
import com.inlurker.komiq.model.data.mangadexapi.constants.SortingOrder
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting

class ComicSearchQuery private constructor(
    var searchQuery: String,
    var comicLanguage: ComicLanguageSetting,
    var sortingMethod: MangaOrderOptions,
    var sortingOrder: String,
    var comicAmount: Int,
    var offsetAmount: Int,
    var includedTags: List<String>,
    var excludedTags: List<String>
) {
    class Builder {
        private var searchQuery: String = ""
        private var comicLanguage: ComicLanguageSetting = ComicLanguageSetting.Korean
        private var sortingMethod: MangaOrderOptions = MangaOrderOptions.FOLLOWED_COUNT
        private var sortingOrder: String = SortingOrder.DESC
        private var comicAmount: Int = 30
        private var offsetAmount: Int = 0
        private var includedTags: List<String> = emptyList()
        private var excludedTags: List<String> = emptyList()

        fun searchQuery(searchQuery: String) = apply { this.searchQuery = searchQuery }
        fun comicLanguage(comicLanguage: ComicLanguageSetting) = apply { this.comicLanguage = comicLanguage }
        fun sortingMethod(sortingMethod: MangaOrderOptions) = apply { this.sortingMethod = sortingMethod }
        fun sortingOrder(sortingOrder: String) = apply { this.sortingOrder = sortingOrder }
        fun comicAmount(comicAmount: Int) = apply { this.comicAmount = comicAmount }
        fun offsetAmount(offsetAmount: Int) = apply { this.offsetAmount = offsetAmount }
        fun includedTags(includedTags: List<String>) = apply { this.includedTags = includedTags }
        fun excludedTags(excludedTags: List<String>) = apply { this.excludedTags = excludedTags }

        fun build() = ComicSearchQuery(
            searchQuery,
            comicLanguage,
            sortingMethod,
            sortingOrder,
            comicAmount,
            offsetAmount,
            includedTags,
            excludedTags
        )
    }

    fun toUrlString(): String {
        val baseUrl = "https://api.mangadex.org/manga"

        val queryParams = mutableListOf<String>()

        queryParams.add("title=$searchQuery")

        includedTags.forEach { tag ->
            queryParams.add("includedTags[]=$tag")
        }

        excludedTags.forEach { tag ->
            queryParams.add("excludedTags[]=$tag")
        }

        queryParams.add("availableTranslatedLanguage[]=${comicLanguage.isoCode}")
        queryParams.add("limit=$comicAmount")
        queryParams.add("offset=$offsetAmount")
        queryParams.add("order[${sortingMethod.option}]=$sortingOrder")
        queryParams.add("includes[]=author")
        queryParams.add("includes[]=artist")
        queryParams.add("includes[]=cover_art")
        queryParams.add("hasAvailableChapters=true")

        val queryString = queryParams.joinToString("&")

        return "$baseUrl?$queryString"
    }

    // Setters
    internal fun setSearchQuery(searchQuery: String): ComicSearchQuery {
        this.searchQuery = searchQuery
        return this
    }

    internal fun setComicLanguage(comicLanguage: ComicLanguageSetting): ComicSearchQuery {
        this.comicLanguage = comicLanguage
        return this
    }

    internal fun setSortingMethod(sortingMethod: MangaOrderOptions): ComicSearchQuery {
        this.sortingMethod = sortingMethod
        return this
    }

    internal fun setSortingOrder(sortingOrder: String): ComicSearchQuery {
        this.sortingOrder = sortingOrder
        return this
    }

    internal fun setComicAmount(comicAmount: Int): ComicSearchQuery {
        this.comicAmount = comicAmount
        return this
    }

    internal fun setOffsetAmount(offsetAmount: Int): ComicSearchQuery {
        this.offsetAmount = offsetAmount
        return this
    }

    internal fun setIncludedTags(includedTags: List<String>): ComicSearchQuery {
        this.includedTags = includedTags
        return this
    }

    internal fun setExcludedTags(excludedTags: List<String>): ComicSearchQuery {
        this.excludedTags = excludedTags
        return this
    }

    // Copy function
    fun copy(
        searchQuery: String? = null,
        comicLanguage: ComicLanguageSetting? = null,
        sortingMethod: MangaOrderOptions? = null,
        sortingOrder: String? = null,
        comicAmount: Int? = null,
        offsetAmount: Int? = null,
        includedTags: List<String>? = null,
        excludedTags: List<String>? = null
    ): ComicSearchQuery {
        return ComicSearchQuery(
            searchQuery ?: this.searchQuery,
            comicLanguage ?: this.comicLanguage,
            sortingMethod ?: this.sortingMethod,
            sortingOrder ?: this.sortingOrder,
            comicAmount ?: this.comicAmount,
            offsetAmount ?: this.offsetAmount,
            includedTags ?: this.includedTags,
            excludedTags ?: this.excludedTags
        )
    }
}