package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.mangadexapi.getComicChapterList


class ComicDetailViewModel(
    comic: Comic
) : ViewModel() {

    var comic by mutableStateOf(comic)
        private set

    var isComicInLibrary by mutableStateOf(false)
        private set

    var genreList by mutableStateOf(comic.tags)
        private set


    suspend fun fetchChapterList() =
        getComicChapterList(comic.id)

    fun toggleComicInLibrary(boolean: Boolean) {
        isComicInLibrary = boolean
    }
}


