package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.mangadexapi.adapters.Chapter
import com.inlurker.komiq.model.mangadexapi.getMangaChapterList
import kotlinx.coroutines.launch


class ComicDetailViewModel(
    comic: Comic
) : ViewModel() {

    var comic by mutableStateOf(comic)
        private set

    var isComicInLibrary by mutableStateOf(false)
        private set

    var chapterList by mutableStateOf<List<Chapter>>(emptyList())
        private set

    var genreList by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            fetchChapterList()
        }
    }

    private suspend fun fetchChapterList() {
        // Simulated asynchronous fetch of chapter preview data
        chapterList = getMangaChapterList(comic.id)
    }

    fun toggleComicInLibrary(boolean: Boolean) {
        isComicInLibrary = boolean
    }
}


