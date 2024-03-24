package com.inlurker.komiq.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.inlurker.komiq.model.data.kotatsu.parsers.chapterToKotatsuMangaChapter
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaPageToPagesUrl
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl
import com.inlurker.komiq.viewmodel.translation.textdetection.CraftTextDetection
import com.inlurker.komiq.viewmodel.utils.scaleCoordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koitharu.kotatsu.parsers.model.MangaSource

class ComicReaderViewModel(application: Application): AndroidViewModel(application) {

    var chapter by mutableStateOf(ComicRepository.currentChapter)

    var pagesUrl by mutableStateOf(emptyList<String>())
        private set

    lateinit var craftTextDetection: CraftTextDetection

    init {
        viewModelScope.launch {
            pagesUrl = if (ComicRepository.currentComic.languageSetting == ComicLanguageSetting.Japanese) {
                val mangaLoaderContext = ComicRepository.getKotatsuLoaderContext(application as Context).newParserInstance(MangaSource.RAWKUMA)
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

    fun initializeCraft(context: Context) {
        craftTextDetection = CraftTextDetection(context)
        craftTextDetection.initialize()
    }

    fun endCraft() {
        craftTextDetection.endInference()
    }

    suspend fun detectText(bitmap: Bitmap): Pair<Double, Array<Array<FloatArray>>> {
        var processingTime = 0.0
        var detectedPixels = arrayOf<Array<FloatArray>>()

        viewModelScope.launch {
            // Start time recorded just before the coroutine begins processing
            val startTime = System.currentTimeMillis()

            // Call the Python module on the IO dispatcher (background thread)
            val detectedCoordinates = withContext(Dispatchers.IO) {
                craftTextDetection.detectText(bitmap)
            }

            // Calculate the processing time
            val endTime = System.currentTimeMillis()
            processingTime = (endTime - startTime) / 1000.0

            detectedPixels = scaleCoordinates(detectedCoordinates, Pair(300,400), Pair(bitmap.width, bitmap.height))
        }
        return Pair(processingTime, detectedPixels)
    }


}
