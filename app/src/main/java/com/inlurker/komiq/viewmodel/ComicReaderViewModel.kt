package com.inlurker.komiq.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
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
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import com.inlurker.komiq.model.data.kotatsu.parsers.chapterToKotatsuMangaChapter
import com.inlurker.komiq.model.data.kotatsu.parsers.kotatsuMangaPageToPagesUrl
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.model.ocr.mangaocr.MangaOCRService
import com.inlurker.komiq.model.translation.caiyun.CaiyunTranslateService
import com.inlurker.komiq.model.translation.deepl.DeeplTranslateService
import com.inlurker.komiq.model.translation.googletranslate.GoogleTranslateService
import com.inlurker.komiq.model.translation.targetlanguages.DeepLTargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.GoogleTLTargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.TargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.caiyun.CaiyunENTargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.caiyun.CaiyunJATargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.caiyun.CaiyunZHTargetLanguage
import com.inlurker.komiq.model.translation.textdetection.CraftTextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextRecognition
import com.inlurker.komiq.ui.screens.helper.Enumerated.TranslationEngine
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl
import com.inlurker.komiq.ui.screens.helper.ReaderHelper.AutomaticTranslationSettingsData
import com.inlurker.komiq.viewmodel.utils.imageutils.drawTranslatedText
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koitharu.kotatsu.parsers.model.MangaSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ComicReaderViewModel(application: Application): AndroidViewModel(application) {
    var chapter by mutableStateOf(ComicRepository.currentChapter)

    val comicLanguage = ComicRepository.currentComic.languageSetting

    private var _autoTranslateSettings = mutableStateOf(
        AutomaticTranslationSettingsData(
            enabled = false,
            sourceLanguage = comicLanguage,
            textDetection = TextDetection.CRAFT,
            textRecognition = TextRecognition.getOptionList(comicLanguage).first(),
            translationEngine = TranslationEngine.Google,
            targetLanguage = GoogleTLTargetLanguage.English
        )
    )

    var autoTranslateSettings: AutomaticTranslationSettingsData
        get() = _autoTranslateSettings.value
        set(value) {
            // Check if there are changes in the relevant properties
            var reloadTranslatedPages = false

            if (value.textDetection != _autoTranslateSettings.value.textDetection ||
                value.textRecognition != _autoTranslateSettings.value.textRecognition ||
                value.translationEngine != _autoTranslateSettings.value.translationEngine ||
                value.targetLanguage != _autoTranslateSettings.value.targetLanguage
            ) {
                if (value.translationEngine != _autoTranslateSettings.value.translationEngine) {
                    value.targetLanguage = getDefaultLanguage(value.translationEngine)
                }

                reloadTranslatedPages = true
            }
            _autoTranslateSettings.value = value

            if (reloadTranslatedPages) {
                resetTranslatedPagesAndProcess()
            }
        }


    var pageUrls by mutableStateOf(emptyList<String>())
        private set

    // Initialize an array of MutableLiveData, assuming a fixed size. Adjust size as necessary.
    var comicPages = arrayOf<MutableLiveData<Bitmap>>()

    var translatedPages = arrayOf<MutableLiveData<Bitmap>>()

    private val pagesInProcess = mutableSetOf<Int>()

    var croppedBitmaps by mutableStateOf(listOf<Bitmap>())

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
                val translations = processAndTranslate(context, pageIndex, bitmap)
                Log.d("TL_Task", "Translation Done")
                val translatedPage = if (translations.isNotEmpty()) {
                    drawTranslatedText(context, bitmap, translations)
                } else {
                    comicPageLiveData
                }
                Log.d("Draw_Task", "Drawing Done")
                translatedPageLiveData.postValue(translatedPage)
            }
        }
    }

    suspend fun processAndTranslate(context: Context, pageIndex: Int, bitmap: Bitmap): List<Pair<BoundingBox, String>> {
        val boundingBoxes = coroutineScope {
            detectText(context, pageIndex, bitmap)  // Assuming detectText is a suspend function
        }

        croppedBitmaps = mutableListOf()

        return coroutineScope {
            boundingBoxes.map { boundingBox ->
                async {
                    val croppedBitmap = cropBitmap(bitmap, boundingBox)
                    croppedBitmaps += croppedBitmap
                    val recognizedText = enqueueToMangaOCR(croppedBitmap)
                    recognizedText?.let {
                        if (!it.hasOnlyGarbage()) {
                            val translatedText = translateText(it)
                            if (translatedText != null) Pair(boundingBox, translatedText) else null
                        } else null
                    }
                }
            }.awaitAll().filterNotNull()
        }
    }

    suspend fun detectText(context: Context, index: Int, bitmap: Bitmap): List<BoundingBox> = coroutineScope {
        if (!isCraftInitialized()) {
            initializeCraft(context)
        }
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
            .target(
                onSuccess = { result ->
                    val bitmap = result.toBitmap()
                    comicPages[index].postValue(bitmap)
                },
                onError = {
                    Log.d("ImageRequest", "Error on retrieving index $index: ${pageUrls[index]}")
                }
            )
            .build()

        imageLoader.execute(imageRequest)
    }

    fun cropBitmap(sourceBitmap: Bitmap, boundingBox: BoundingBox): Bitmap {
        // Calculate the width and height of the cropped area
        val width = (boundingBox.X2 - boundingBox.X1).toInt()
        val height = (boundingBox.Y2 - boundingBox.Y1).toInt()

        // Crop the bitmap based on the bounding box coordinates
        return Bitmap.createBitmap(sourceBitmap, boundingBox.X1.toInt(), boundingBox.Y1.toInt(), width, height)
    }

    suspend fun translateText(text: String): String? {
        return when (autoTranslateSettings.translationEngine) {
            TranslationEngine.Google -> enqueueToGoogleTranslate(text)
            TranslationEngine.DeepL -> enqueueToDeepL(text)
            TranslationEngine.Caiyun -> enqueueToCaiyun(text)
            else -> null
        }
    }

    suspend fun enqueueToMangaOCR(bitmap: Bitmap): String? = coroutineScope {
        suspendCoroutine { cont ->
            MangaOCRService.enqueueOCRRequest(bitmap) { ocrResult ->
                val processedResult = ocrResult?.removeSpaces()
                Log.d("OCR", "Recognized Text: $processedResult") // Logging the recognized text
                cont.resume(processedResult) // Resume with null after the second attempt
            }
        }
    }


    suspend fun enqueueToGoogleTranslate(sourceText: String):String? = coroutineScope {
        suspendCoroutine { cont ->
            GoogleTranslateService.enqueueTranslateRequest(
                sourceText, comicLanguage.isoCode, autoTranslateSettings.targetLanguage.isoCode
            ) { result ->
                Log.d("GoogleTL", "Translated Text: $result") // Logging the translated text
                cont.resume(result)
            }
        }
    }

    suspend fun enqueueToDeepL(sourceText: String):String? = coroutineScope {
        suspendCoroutine { cont ->
            comicLanguage.toDeepLIsoCode()?.let { sourceLanguage ->
                DeeplTranslateService.enqueueTranslateRequest(
                    sourceText, sourceLanguage, autoTranslateSettings.targetLanguage.isoCode
                ) { result ->
                    Log.d("DeepL", "Translated Text: $result") // Logging the translated text
                    cont.resume(result)
                }
            }
        }
    }

    suspend fun enqueueToCaiyun(sourceText: String):String? = coroutineScope {
        suspendCoroutine { cont ->
            CaiyunTranslateService.enqueueTranslateRequest(
                sourceText, autoTranslateSettings.targetLanguage.isoCode
            ) { result ->
                Log.d("Caiyun", "Translated Text: $result") // Logging the translated text
                cont.resume(result)
            }
        }
    }

    fun isCraftInitialized() = ::craftTextDetection.isInitialized

    private fun resetTranslatedPagesAndProcess() {
        for(i in translatedPages.indices) {
            translatedPages[i].postValue(null)  // Set to null or a default Bitmap
        }
        pagesInProcess.clear()
    }

    private fun getDefaultLanguage(translationEngine: TranslationEngine): TargetLanguage {
        return when (translationEngine) {
            TranslationEngine.DeepL -> DeepLTargetLanguage.English_US
            TranslationEngine.Google -> GoogleTLTargetLanguage.English
            TranslationEngine.Caiyun -> {
                when (comicLanguage) {
                    ComicLanguageSetting.English -> CaiyunENTargetLanguage.Chinese
                    ComicLanguageSetting.Chinese -> CaiyunZHTargetLanguage.English
                    ComicLanguageSetting.Japanese -> CaiyunJATargetLanguage.Chinese
                    else -> CaiyunENTargetLanguage.Chinese
                }
            }
            else -> GoogleTLTargetLanguage.English
        }
    }

    private fun String?.removeSpaces(): String? {
        return this?.replace(" ", "")
    }

    private fun String.hasOnlyGarbage(): Boolean {
        // This regex checks if the string does not contain any Unicode letter or number.
        // `\\p{L}` matches any letter from any language, and `\\p{N}` matches any numeric digit.
        return !this.any { it.toString().matches(Regex("[\\p{L}\\p{N}]")) }
    }

    fun disposeCraft() {
        if (isCraftInitialized()) {
            endCraft()  // Call endCraft when the composable is disposed
        }
    }
}

