package com.inlurker.komiq.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.kotatsu.parsers.chapterToKotatsuMangaChapter
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaPageToPagesUrl
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl
import kotlinx.coroutines.launch
import org.koitharu.kotatsu.parsers.model.MangaSource

class ComicReaderViewModel: ViewModel() {

    var chapter by mutableStateOf(ComicRepository.currentChapter)

    var pagesUrl by mutableStateOf(emptyList<String>())
        private set

    suspend fun getPages(context: Context) {
        viewModelScope.launch {
            pagesUrl = if (ComicRepository.currentComic.languageSetting == ComicLanguageSetting.Japanese) {
                val mangaLoaderContext = ComicRepository.getKotatsuLoaderContext(context).newParserInstance(MangaSource.RAWKUMA)
                val mangaPages = mangaLoaderContext.getPages(
                    chapterToKotatsuMangaChapter(chapter)
                )

                kotatsuMangaPageToPagesUrl(mangaPages)
            } else {
                val chapterPages = ComicRepository.getChapterPages(chapter.id)!!

                chapterPages.data.map { filename ->
                    getChapterPageImageUrl(chapterPages.hash, filename)
                }
            }
        }
    }
}
