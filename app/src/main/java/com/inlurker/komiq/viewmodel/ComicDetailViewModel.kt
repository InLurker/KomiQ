package com.inlurker.komiq.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.datamodel.Tag
import com.inlurker.komiq.model.data.kotatsu.parsers.comicToKotatsuManga
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaChapterListToChapterList
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaToComic
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import kotlinx.coroutines.launch
import org.koitharu.kotatsu.parsers.InternalParsersApi
import org.koitharu.kotatsu.parsers.PagedMangaParser
import org.koitharu.kotatsu.parsers.model.MangaSource


@OptIn(InternalParsersApi::class)
class ComicDetailViewModel : ViewModel() {
    var comic by mutableStateOf(
        ComicRepository.currentComic
    )
        private set

    var isComicInLibrary by mutableStateOf(false)
        private set

    var genreList by mutableStateOf(emptyList<Tag>())
        private set

    var chapterList by mutableStateOf(emptyList<Chapter>())
        private set

    suspend fun getComicDetail(context: Context) {
        viewModelScope.launch {
            if (comic.languageSetting == ComicLanguageSetting.Japanese) {
                val kotatsuParser = ComicRepository
                    .getKotatsuLoaderContext(context)
                    .newParserInstance(MangaSource.RAWKUMA)
                        as PagedMangaParser

                val kotatsuManga = kotatsuParser.getDetails(
                    comicToKotatsuManga(comic)
                )
                comic = kotatsuMangaToComic(
                    kotatsuManga
                )
                genreList = comic.tags

                val chapters = kotatsuManga.getChapters(null)

                chapters?.let {
                    chapterList = kotatsuMangaChapterListToChapterList(chapters)
                }
            }
            else {
                ComicRepository.getComic(comic.id, comic.languageSetting)?.let {
                    comic = it
                }

                genreList = comic.tags

                chapterList = ComicRepository.getComicChapterList(comic.id, comic.languageSetting)

                ComicRepository.isComicInLibrary(comic.id, comic.languageSetting).collect { isInLibrary ->
                    isComicInLibrary = isInLibrary
                }
            }
        }
    }


    fun toggleComicInLibrary() {
        viewModelScope.launch {
            val success = if (isComicInLibrary) {
                ComicRepository.removeComicFromLibrary(comic.id, comic.languageSetting)
            } else {
                ComicRepository.addComicToLibrary(comic)
            }

            if (success) {
                isComicInLibrary = !isComicInLibrary
            }
        }
    }
}


