package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.datamodel.Tag
import com.inlurker.komiq.model.data.repository.ComicRepository
import kotlinx.coroutines.launch


class ComicDetailViewModel(
    comicId: String
) : ViewModel() {


    var comic by mutableStateOf(Comic())
        private set

    var isComicInLibrary by mutableStateOf(false)
        private set

    var genreList by mutableStateOf(emptyList<Tag>())
        private set


    var chapterList by mutableStateOf(emptyList<Chapter>())
        private set

    init {
        viewModelScope.launch {
            ComicRepository.getComic(comicId)?.let {
                comic = it
            }

            genreList = comic.tags

            chapterList = ComicRepository.getComicChapterList(comicId)

            ComicRepository.isComicInLibrary(comicId).collect { isInLibrary ->
                isComicInLibrary = isInLibrary
            }
        }
    }

    fun toggleComicInLibrary() {
        viewModelScope.launch {
            val success = if (isComicInLibrary) {
                ComicRepository.removeComicFromLibrary(comic.id)
            } else {
                ComicRepository.addComicToLibrary(comic)
            }

            if (success) {
                isComicInLibrary = !isComicInLibrary
            }
        }
    }
}


