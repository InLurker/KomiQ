package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.ui.screens.MangaChapterPreview
import java.util.Date


class ComicDetailViewModel(
    comic: Comic
) : ViewModel() {

    var comic by mutableStateOf(comic)
        private set

    var isComicInLibrary by mutableStateOf(false)
        private set

    var chapterPreview by mutableStateOf<MangaChapterPreview?>(null)
        private set

    var genreList by mutableStateOf<List<String>>(emptyList())
        private set

    var comicCoverUrl by mutableStateOf("https://uploads.mangadex.org/covers/${comic.id}/${comic.cover}.512.jpg")
        private set

    init {
        fetchChapterPreview()
        fetchGenreList()
    }

    private fun fetchChapterPreview() {
        // Simulated asynchronous fetch of chapter preview data
        val chapterPreview = MangaChapterPreview(
            volumeNumber = 1,
            chapterNumber = 23,
            chapterName = "Chapter Name",
            uploadDate = Date(),
            scanlationGroup = "Scanlation Group"
        )
        this.chapterPreview = chapterPreview
    }

    private fun fetchGenreList() {
        // Simulated asynchronous fetch of genre list data
        val genreList = listOf(
            "Comedy",
            "Drama",
            "Historical",
            "Medical",
            "Mystery",
            "NewGenre",
            "NewGenre"
        )
        this.genreList = genreList
    }

    fun toggleComicInLibrary(boolean: Boolean) {
        isComicInLibrary = boolean
    }
}


