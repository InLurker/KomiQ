package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.model.mangadexapi.builders.ComicSearchQuery
import com.inlurker.komiq.model.mangadexapi.constants.MangaOrderOptions
import com.inlurker.komiq.model.mangadexapi.constants.SortingOrder
import com.inlurker.komiq.viewmodel.paging.ListState
import kotlinx.coroutines.launch

class DiscoverViewModel : ViewModel() {
    private val comicRepository = ComicRepository

    var comicList= mutableStateListOf<Comic>()

    private var currentPage = 1
    private var isPaginationExhausted = false
    private var isLoading = false

    var searchQuery by mutableStateOf("")
    var sortingMethod  by mutableStateOf(MangaOrderOptions.RELEVANCE)
    var sortingOrder by mutableStateOf(SortingOrder.DESC)
    var comicAmount = 30
    var offsetAmount by mutableStateOf(0)
    var includedTags  by mutableStateOf(emptyList<String>())
    var excludedTags by mutableStateOf(emptyList<String>())


    var listState by mutableStateOf(ListState.IDLE)

    private val comicSearchQuery = ComicSearchQuery.Builder()
        .searchQuery(searchQuery)
        .sortingMethod(sortingMethod)
        .sortingOrder(sortingOrder)
        .comicAmount(comicAmount)
        .offsetAmount(offsetAmount)
        .includedTags(includedTags)
        .excludedTags(excludedTags)
        .comicAmount(30)
        .offsetAmount((currentPage - 1) * 30)
        .build()

    init {
        getComics()
    }

    fun getComics() {
        if (isLoading || isPaginationExhausted) return

        viewModelScope.launch {
            try {
                isLoading = true
                listState = ListState.LOADING

                val updatedComicSearchQuery = comicSearchQuery.copy(offsetAmount = (currentPage - 1) * 30)

                comicRepository.getComicList(updatedComicSearchQuery)
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
    }


    fun loadNextPage() {
        if (!isLoading && !isPaginationExhausted) {
            getComics()
        }
    }
}
