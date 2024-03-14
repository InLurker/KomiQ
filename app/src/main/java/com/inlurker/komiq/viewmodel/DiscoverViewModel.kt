package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.mangadexapi.builders.ComicSearchQuery
import com.inlurker.komiq.model.data.mangadexapi.constants.GenreTag
import com.inlurker.komiq.model.data.mangadexapi.constants.MangaOrderOptions
import com.inlurker.komiq.model.data.mangadexapi.constants.SortingOrder
import com.inlurker.komiq.model.data.mangadexapi.constants.ThemeTag
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.viewmodel.paging.ListState
import kotlinx.coroutines.launch

class DiscoverViewModel : ViewModel() {
    var comicList = mutableStateListOf<Comic>()

    private var currentPage = 1
    private var isPaginationExhausted = false
    private var isLoading = false

    var searchQuery by mutableStateOf("")
    var sortingMethod by mutableStateOf(MangaOrderOptions.FOLLOWED_COUNT)
    var sortingOrder by mutableStateOf(SortingOrder.DESC)
    val comicAmount = 30
    var offsetAmount by mutableStateOf(0)
    var comicLanguageSetting by mutableStateOf(ComicLanguageSetting.English)
    var genreFilter by mutableStateOf(emptyList<GenreTag>())
    var themeFilter by mutableStateOf(emptyList<ThemeTag>())


    var listState by mutableStateOf(ListState.IDLE)

    private var comicSearchQuery = ComicSearchQuery.Builder()
        .searchQuery(searchQuery)
        .comicLanguage(comicLanguageSetting)
        .sortingMethod(sortingMethod)
        .sortingOrder(sortingOrder)
        .comicAmount(comicAmount)
        .offsetAmount(offsetAmount)
        .includedTags(genreFilter.map { it.hash } + themeFilter.map { it.hash })
        .comicAmount(30)
        .offsetAmount((currentPage - 1) * 30)
        .build()


    suspend fun getComics() {
        if (isLoading || isPaginationExhausted) return

        try {
            isLoading = true
            listState = ListState.LOADING

            val updatedComicSearchQuery =
                comicSearchQuery.copy(offsetAmount = (currentPage - 1) * 30)

            ComicRepository.getComicList(updatedComicSearchQuery, comicLanguageSetting)
                .collect { comics ->
                    if (comics.isNotEmpty()) {
                        comicList.addAll(comics)
                        listState = ListState.IDLE
                        currentPage++
                    } else {
                        isPaginationExhausted = true
                        listState = ListState.PAGINATION_EXHAUST
                    }
                }
        } catch (error: Exception) {
            listState = ListState.ERROR
            // Handle error
        } finally {
            isLoading = false
        }
    }

    suspend fun loadNextPage() {
        if (!isLoading && !isPaginationExhausted) {
            getComics()
        }
    }

    fun resetComicList() {
        comicList.clear()
        currentPage = 1
        isPaginationExhausted = false
        isLoading = false
        viewModelScope.launch {
            getComics()
        }
    }

    fun updateSearchQuery() {
        comicSearchQuery
            .setSearchQuery(searchQuery)
            .setComicLanguage(comicLanguageSetting)
            .setSortingMethod(sortingMethod)
            .setSortingOrder(sortingOrder)
            .setComicAmount(comicAmount)
            .setOffsetAmount(offsetAmount)
            .setIncludedTags(genreFilter.map { it.hash })
            .setComicAmount(30)
            .setOffsetAmount((currentPage - 1) * 30)
    }
}
