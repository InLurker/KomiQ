package com.inlurker.komiq.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import com.inlurker.komiq.model.data.kotatsu.parsers.chapterToKotatsuMangaChapter
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaPageToPagesUrl
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl
import com.inlurker.komiq.viewmodel.translation.textdetection.CraftTextDetection
import com.inlurker.komiq.viewmodel.utils.drawBoundingBoxes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koitharu.kotatsu.parsers.model.MangaSource

class ComicReaderViewModel(application: Application): AndroidViewModel(application) {

    var chapter by mutableStateOf(ComicRepository.currentChapter)

    var pageUrls by mutableStateOf(emptyList<String>())
        private set

    // Initialize an array of MutableLiveData, assuming a fixed size. Adjust size as necessary.
    var comicPages = arrayOf<MutableLiveData<Bitmap>>()

    var translatedPages = arrayOf<MutableLiveData<Bitmap>>()

    fun updateComicPage(index: Int, page: Bitmap) {
        if (index < comicPages.size) {
            comicPages[index].value = page
        }
    }

    fun updateTranslatedPage(index: Int, page: Bitmap) {
        if (index < translatedPages.size) {
            translatedPages[index].value = page
        }
    }

    lateinit var craftTextDetection: CraftTextDetection
    lateinit var imageLoader: ImageLoader

    init {
        viewModelScope.launch {
            pageUrls =
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

            comicPages = Array(pageUrls.size) { MutableLiveData<Bitmap>() }
            translatedPages = Array(pageUrls.size) { MutableLiveData<Bitmap>() }

            imageLoader = ImageLoader(application as Context)
        }
    }

    fun initializeCraft(context: Context) {
        craftTextDetection = CraftTextDetection(context)
    }

    fun endCraft() {
        craftTextDetection.endInference()
    }

    fun getComicPage(index: Int, context: Context): LiveData<Bitmap> {
        val page = comicPages[index]

        if (page.value == null) {
            viewModelScope.launch {
                fetchComicPage(index, context)
            }
        }

        return page
    }

    fun getTranslatedPage(index: Int, context: Context): LiveData<Bitmap> {
        val comicPageFlow = getComicPage(index, context).asFlow()

        // Launch a coroutine to wait for the comic page to be non-null and then proceed with translation
        viewModelScope.launch {
            comicPageFlow.collect { _ ->
                // At this point, bitmap is not null. Now, trigger the translation.
                translatePage(index)
            }
        }

        // Return the LiveData directly. The above coroutine will update it when the translation is done.
        return translatedPages[index]
    }

    fun translatePage(index: Int) {
        val translatedPageLiveData = translatedPages[index]

        // Check if there's already a translated page or if the comic page is null
        if (translatedPageLiveData.value != null) {
            return
        }

        val comicPageLiveData = comicPages[index].value

        comicPageLiveData?.let { bitmap ->

            detectText(
                index,
                bitmap,
                onResult = { _, boundingBoxes ->
                    // Use postValue since this might be called from a background thread
                    translatedPageLiveData.postValue(drawBoundingBoxes(bitmap, boundingBoxes))
                }
            )
        }
    }

    fun detectText(
        index: Int,
        bitmap: Bitmap,
        onResult: (Double, List<BoundingBox>) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        // Start time recorded just before the coroutine begins processing
        val startTime = System.currentTimeMillis()

        // Call the Python module on the IO dispatcher (background thread)
        craftTextDetection.queueDetectText(index, bitmap) { boundingBoxes ->
            // Calculate the processing time
            val endTime = System.currentTimeMillis()
            val processingTime = (endTime - startTime) / 1000.0

            onResult(processingTime, boundingBoxes)
        }
    }

    suspend fun fetchComicPage(index: Int, context: Context) {
        val comicPageLiveData = comicPages[index].value
        if (comicPageLiveData != null) {
            return
        }

        val imageRequest = ImageRequest.Builder(context)
            .data(pageUrls[index])
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
        val result = imageLoader.execute(imageRequest).drawable as BitmapDrawable
        val bitmap = result.toBitmap()

        comicPages[index].postValue(bitmap)
    }

    fun isCraftInitialized() = ::craftTextDetection.isInitialized
}

