package com.inlurker.komiq.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
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

    var comicPages by mutableStateOf(arrayOf<Bitmap?>())
    var translatedPages by mutableStateOf(arrayOf<Bitmap?>())

    lateinit var craftTextDetection: CraftTextDetection
    lateinit var imageLoader: ImageLoader

    init {
        viewModelScope.launch {
            pagesUrl =
                if (ComicRepository.currentComic.languageSetting == ComicLanguageSetting.Japanese) {
                    val mangaLoaderContext =
                        ComicRepository.getKotatsuLoaderContext(application as Context)
                            .newParserInstance(MangaSource.RAWKUMA)
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
            comicPages = arrayOfNulls(pagesUrl.size)
            translatedPages = arrayOfNulls(pagesUrl.size)

            imageLoader = ImageLoader(application as Context)
        }
    }

    fun initializeCraft(context: Context) {
        craftTextDetection = CraftTextDetection(context as Activity)
        craftTextDetection.initialize()
    }

    fun endCraft() {
        craftTextDetection.endInference()
    }

    fun detectText(
        bitmap: Bitmap,
        onResult: (Double, Array<Array<FloatArray>>) -> Unit
    ): Pair<Double, Array<Array<FloatArray>>> {
        var processingTime = 0.0
        var detectedPixels = arrayOf<Array<FloatArray>>()
        viewModelScope.launch(Dispatchers.IO) {

            // Start time recorded just before the coroutine begins processing
            val startTime = System.currentTimeMillis()

            craftTextDetection.initialize()
            // Call the Python module on the IO dispatcher (background thread)
            val detectedCoordinates = withContext(Dispatchers.IO) {
                craftTextDetection.detectText(bitmap)
            }
            craftTextDetection.endInference()
            // Calculate the processing time
            val endTime = System.currentTimeMillis()
            processingTime = (endTime - startTime) / 1000.0

            detectedPixels = scaleCoordinates(
                detectedCoordinates,
                Pair(300, 400),
                Pair(bitmap.width, bitmap.height)
            )

            withContext(Dispatchers.Main) { // Switch back to the Main thread to return the result
                onResult(processingTime, detectedPixels)
            }

        }
        return Pair(processingTime, detectedPixels)
    }

    suspend fun fetchComicPage(pageNumber: Int, context: Context): Bitmap {
        comicPages[pageNumber]?.let {
            return it
        }

        val imageRequest = ImageRequest.Builder(context)
            .data(pagesUrl[pageNumber])
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
        val result = imageLoader.execute(imageRequest).drawable as BitmapDrawable
        val bitmap = result.toBitmap()

        comicPages[pageNumber] = bitmap
        return bitmap
    }
}

