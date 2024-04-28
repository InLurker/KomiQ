package com.inlurker.komiq.model.recognition.mlkit

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.recognition.helper.removeSpaces
import com.inlurker.komiq.model.recognition.helper.reverseAndCombineLines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max

class MLKitService(val language: ComicLanguageSetting) {
    private val textRecognizer: TextRecognizer
    private val requestQueue = Channel<TextRecognitionJob>(Channel.UNLIMITED)

    data class TextRecognitionJob(
        val bitmap: Bitmap,
        val callback: (Any?) -> Unit,
        val isDetection: Boolean = false // Default is false for recognition tasks
    )

    init {
        textRecognizer = when (language) {
            ComicLanguageSetting.Japanese -> TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
            ComicLanguageSetting.Chinese,
            ComicLanguageSetting.TraditionalChinese -> TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
            ComicLanguageSetting.Hindi -> TextRecognition.getClient(
                DevanagariTextRecognizerOptions.Builder().build())

            ComicLanguageSetting.Korean -> TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
            else -> TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) // Default for Latin-based
        }
        CoroutineScope(Dispatchers.IO).launch {
            requestQueue.consumeAsFlow().collect { job ->
                processTextRecognition(job)
            }
        }
    }

    fun enqueueTextRecognition(bitmap: Bitmap, callback: (String?) -> Unit) {
        @Suppress("UNCHECKED_CAST") val job = TextRecognitionJob(bitmap, callback as (Any?) -> Unit)
        CoroutineScope(Dispatchers.IO).launch {
            requestQueue.send(job)
        }
    }

    fun enqueueTextDetection(bitmap: Bitmap, callback: (List<Pair<BoundingBox, String>>) -> Unit) {
        @Suppress("UNCHECKED_CAST") val job = TextRecognitionJob(bitmap, callback as (Any?) -> Unit, isDetection = true)
        CoroutineScope(Dispatchers.IO).launch {
            requestQueue.send(job)
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private suspend fun processTextRecognition(job: TextRecognitionJob) {
        val resizedBitmap = resizeBitmapIfNecessary(job.bitmap)
        val inputImage = InputImage.fromBitmap(resizedBitmap, 0)
        val result = if (job.isDetection) {
            try {
                withContext(Dispatchers.IO) {
                    suspendCoroutine<List<Pair<BoundingBox, String>>> { continuation ->
                        textRecognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                val boxesWithText = visionText.textBlocks.map { block ->
                                    block.boundingBox?.let { boundingBox ->
                                        val recognizedText = if(language == ComicLanguageSetting.Japanese || language == ComicLanguageSetting.Chinese) {
                                            block.text.reverseAndCombineLines().removeSpaces()
                                        } else block.text
                                        Pair(boundingBox.toBoundingBox(), recognizedText)
                                    }
                                }.filterNotNull()
                                continuation.resume(boxesWithText)
                            }
                            .addOnFailureListener { e ->
                                Log.d("MLKit Error", e.message.toString())
                            }
                    }
                }
            } catch (e: Exception) {
                null
            }
        } else {
            try {
                withContext(Dispatchers.IO) {
                    suspendCoroutine<String?> { continuation ->
                        textRecognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                continuation.resume(visionText.text)
                            }
                            .addOnFailureListener { e ->
                                Log.d("MLKit Error", "Error processing text: ${e.message}")
                                continuation.resumeWithException(e)  // Ensure to resume the coroutine with an exception
                            }
                    }
                }
            } catch (e: Exception) {
                null
            }
        }

        withContext(Dispatchers.Main) {
            job.callback(result)
        }
    }

    fun shutdown() {
        textRecognizer.close()
    }

    fun Rect.toBoundingBox(): BoundingBox {
        return BoundingBox(
            this.left.toFloat(),
            this.top.toFloat(),
            this.right.toFloat(),
            this.bottom.toFloat()
        )
    }

    fun resizeBitmapIfNecessary(bitmap: Bitmap): Bitmap {
        val minWidthHeight = 32
        val width = bitmap.width
        val height = bitmap.height

        // Check if the bitmap meets the minimum size requirement
        if (width >= minWidthHeight && height >= minWidthHeight) {
            return bitmap
        }

        // Calculate the scale to use, ensuring neither width nor height is less than 32
        val scaleWidth = minWidthHeight.toFloat() / width
        val scaleHeight = minWidthHeight.toFloat() / height
        val scale = max(scaleWidth, scaleHeight)

        // Calculate the new dimensions
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        // Create a new bitmap with the new dimensions
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
