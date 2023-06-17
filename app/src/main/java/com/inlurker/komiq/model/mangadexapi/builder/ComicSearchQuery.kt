package com.inlurker.komiq.model.mangadexapi.builder

import com.inlurker.komiq.model.mangadexapi.constants.MangaOrderOptions
import com.inlurker.komiq.model.mangadexapi.constants.SortingOrder


class ComicSearchQuery private constructor(
    val searchQuery: String,
    val sortingMethod: String,
    val sortingOrder: String,
    val comicAmount: Int,
    val offsetAmount: Int,
    val includedTags: List<String>,
    val excludedTags: List<String>
) {
    class Builder {
        private var searchQuery: String = ""
        private var sortingMethod: String = MangaOrderOptions.RELEVANCE
        private var sortingOrder: String = SortingOrder.ASC
        private var comicAmount: Int = 30
        private var offsetAmount: Int = 0
        private var includedTags: List<String> = emptyList()
        private var excludedTags: List<String> = emptyList()

        fun searchQuery(searchQuery: String) = apply { this.searchQuery = searchQuery }
        fun sortingMethod(sortingMethod: String) = apply { this.sortingMethod = sortingMethod }
        fun sortingOrder(sortingOrder: String) = apply { this.sortingOrder = sortingOrder }
        fun comicAmount(comicAmount: Int) = apply { this.comicAmount = comicAmount }
        fun offsetAmount(offsetAmount: Int) = apply { this.offsetAmount = offsetAmount }
        fun includedTags(includedTags: List<String>) = apply { this.includedTags = includedTags }
        fun excludedTags(excludedTags: List<String>) = apply { this.excludedTags = excludedTags }

        fun build() = ComicSearchQuery(
            searchQuery,
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

        queryParams.add("limit=$comicAmount")
        queryParams.add("offset=$offsetAmount")
        queryParams.add("order[$sortingMethod]=$sortingOrder")
        queryParams.add("includes[]=author")
        queryParams.add("includes[]=artist")
        queryParams.add("includes[]=cover_art")

        val queryString = queryParams.joinToString("&")

        return "$baseUrl?$queryString"
    }
}