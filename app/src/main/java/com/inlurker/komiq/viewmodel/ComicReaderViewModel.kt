package com.inlurker.komiq.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
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
import com.inlurker.komiq.BuildConfig
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import com.inlurker.komiq.model.data.kotatsu.parsers.chapterToKotatsuMangaChapter
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaPageToPagesUrl
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.model.translation.googletranslate.GoogleTranslateService
import com.inlurker.komiq.model.translation.mangaocr.MangaOCRService
import com.inlurker.komiq.model.translation.textdetection.CraftTextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextRecognition
import com.inlurker.komiq.ui.screens.helper.Enumerated.TranslationEngine
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl
import com.inlurker.komiq.ui.screens.helper.ReaderHelper.AutomaticTranslationSettingsData
import com.inlurker.komiq.viewmodel.utils.imageutils.drawTranslatedText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koitharu.kotatsu.parsers.model.MangaSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ComicReaderViewModel(application: Application): AndroidViewModel(application) {
    private val apiKey = BuildConfig.HUGGINGFACE_API_TOKEN  // Use your actual Hugging Face API key

    var chapter by mutableStateOf(ComicRepository.currentChapter)

    val comicLanguage = ComicRepository.currentComic.languageSetting

    var autoTranslateSettings by mutableStateOf(
        AutomaticTranslationSettingsData(
            enabled = false,
            sourceLanguage = comicLanguage,
            textDetection = TextDetection.CRAFT,
            textRecognition = TextRecognition.getOptionList(comicLanguage).first(),
            translationEngine = TranslationEngine.Google
        )
    )

    var pageUrls by mutableStateOf(emptyList<String>())
        private set

    // Initialize an array of MutableLiveData, assuming a fixed size. Adjust size as necessary.
    var comicPages = arrayOf<MutableLiveData<Bitmap>>()

    var translatedPages = arrayOf<MutableLiveData<Bitmap>>()

    private val pagesInProcess = mutableSetOf<Int>()

    var croppedBitmaps by mutableStateOf(listOf<Bitmap>())

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

    fun getComicPage(pageIndex: Int, context: Context): LiveData<Bitmap> {
        val page = comicPages[pageIndex]

        if (page.value == null) {
            viewModelScope.launch {
                fetchComicPage(pageIndex, context)
            }
        }

        return page
    }

    fun getTranslatedPage(pageIndex: Int, context: Context): LiveData<Bitmap> {
        if (pagesInProcess.contains(pageIndex)) {
            // Page is already being processed, so just return
            return translatedPages[pageIndex]
        }


        if (!isCraftInitialized()) {
            craftTextDetection = CraftTextDetection(context)
        }

        pagesInProcess.add(pageIndex)

        val comicPageFlow = getComicPage(pageIndex, context).asFlow()

        // Launch a coroutine to wait for the comic page to be non-null and then proceed with translation
        viewModelScope.launch {
            comicPageFlow.collect { _ ->
                // At this point, bitmap is not null. Now, trigger the translation.
                translatePage(pageIndex, context)
            }
        }

        // Return the LiveData directly. The above coroutine will update it when the translation is done.
        return translatedPages[pageIndex]
    }

    private suspend fun translatePage(pageIndex: Int, context: Context) {
        val translatedPageLiveData = translatedPages[pageIndex]

        // Check if there's already a translated page or if the comic page is null
        if (translatedPageLiveData.value != null) {
            return
        }

        val comicPageLiveData = comicPages[pageIndex].value

        comicPageLiveData?.let { bitmap ->
            viewModelScope.launch {  // Ensure this scope runs within the suspend function
                val translations = processAndTranslate(pageIndex, bitmap)

                // Now draw the translated text after the translations are completed
                val translatedPage = drawTranslatedText(context, bitmap, translations)
                translatedPageLiveData.postValue(translatedPage)
            }
        }
    }

    suspend fun processAndTranslate(pageIndex: Int, bitmap: Bitmap): List<Pair<BoundingBox, String>> {
        val boundingBoxes = withContext(Dispatchers.IO) {
            detectText(pageIndex, bitmap)  // Assuming detectText is a suspend function
        }
        val croppedBitmaps = cropBitmaps(bitmap, boundingBoxes)
        this.croppedBitmaps = croppedBitmaps

        // Process OCR and translation in parallel and then collect results
        return coroutineScope {
            croppedBitmaps.mapIndexed { idx, croppedBitmap ->
                async {
                    val recognizedText = enqueueToMangaOCR(croppedBitmap, apiKey)  // Ensure enqueueToMangaOCR is a suspend function
                    Log.d("OCR", "Recognized Text for index $idx: $recognizedText") // Logging the recognized text
                    recognizedText?.let {
                        if (!it.hasOnlyGarbage()) {
                            val translatedText = enqueueToGoogleTranslate(it)  // Ensure enqueueToGoogleTranslate is a suspend function
                            Log.d("GoogleTL", "Translated Text for index $idx: $translatedText") // Logging the translated text
                            if (translatedText != null) Pair(boundingBoxes[idx], translatedText) else null
                        } else null
                    }
                }
            }.awaitAll().filterNotNull()
        }
    }

    suspend fun detectText(index: Int, bitmap: Bitmap): List<BoundingBox> = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            craftTextDetection.queueDetectText(index, bitmap) { boundingBoxes ->
                continuation.resume(boundingBoxes)
            }
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

    fun cropBitmaps(sourceBitmap: Bitmap, boundingBoxes: List<BoundingBox>): List<Bitmap> {
        val croppedBitmaps = mutableListOf<Bitmap>()
        for (box in boundingBoxes) {
            croppedBitmaps.add(cropBitmap(sourceBitmap, box))
        }
        return croppedBitmaps
    }

    fun cropBitmap(sourceBitmap: Bitmap, boundingBox: BoundingBox): Bitmap {
        // Calculate the width and height of the cropped area
        val width = (boundingBox.X2 - boundingBox.X1).toInt()
        val height = (boundingBox.Y2 - boundingBox.Y1).toInt()

        // Crop the bitmap based on the bounding box coordinates
        return Bitmap.createBitmap(sourceBitmap, boundingBox.X1.toInt(), boundingBox.Y1.toInt(), width, height)
    }

    suspend fun enqueueToMangaOCR(bitmap: Bitmap, apiKey: String): String? = withContext(Dispatchers.IO) {
        suspendCoroutine { cont ->
            MangaOCRService.enqueueOCRRequest(bitmap, apiKey) { result ->
                cont.resume(result.removeSpaces())
            }
        }
    }

    suspend fun enqueueToGoogleTranslate(sourceText: String):String? = withContext(Dispatchers.IO) {
        suspendCoroutine { cont ->
            GoogleTranslateService.enqueueTranslateRequest(
                sourceText, comicLanguage.isoCode, ComicLanguageSetting.English.isoCode
            ) { result ->
                cont.resume(result)
            }
        }
    }

    fun isCraftInitialized() = ::craftTextDetection.isInitialized

    private fun String?.removeSpaces(): String? {
        return this?.replace(" ", "")
    }

    private fun String.hasOnlyGarbage(): Boolean {
        // This regex checks if the string does not contain any Unicode letter or number.
        // `\\p{L}` matches any letter from any language, and `\\p{N}` matches any numeric digit.
        return !this.any { it.toString().matches(Regex("[\\p{L}\\p{N}]")) }
    }
}

