package com.inlurker.komiq.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPages
import com.inlurker.komiq.model.data.repository.ComicRepository
import kotlinx.coroutines.launch

class ComicReaderViewModel(
    private val chapterId: String
) : ViewModel() {

    var chapter by mutableStateOf<Chapter?>(null)

    var chapterPages by mutableStateOf<ChapterPages?>(null)
        private set

    init {
        viewModelScope.launch {
            chapter = ComicRepository.getChapter(chapterId)
            chapterPages = ComicRepository.getChapterPages(chapterId)
        }
    }
}
