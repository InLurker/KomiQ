package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.repository.ComicRepository
import kotlinx.coroutines.launch


class LibraryViewModel : ViewModel() {

    var comics by mutableStateOf(emptyList<Comic>())
        private set

    init {
        loadComicsFromDatabase()
    }

    private fun loadComicsFromDatabase() {
        viewModelScope.launch {
            ComicRepository.getComicLibrary().collect { libraryComics ->
                comics = libraryComics
            }
        }
    }
}