
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.wait
import java.io.File
import java.io.FileOutputStream
import java.util.Collections
import java.util.LinkedList

class PaddleOCRService(private val context: Context) {
    private val pythonInstance: Python
    private val module: PyObject
    private val ocrQueue: MutableList<OCRJob>

    private data class OCRJob(
        val bitmap: Bitmap,
        val language: String?,
        val callback: (Any?) -> Unit,
        val isRecognition: Boolean  // To distinguish between detection and recognition
    )
    private data class CoordinatesWithText(
        val points: List<List<Float>>,
        val texts: TextResponse
    )

    private data class TextResponse(
        val text: String,
        val confidence: Double
    )

    val regexPattern = """\[\[((?:\[\d+\.\d+, \d+\.\d+],? ?)+)], \('(.*?)', (\d+\.\d+)\)]""".toRegex()


    init {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }
        pythonInstance = Python.getInstance()
        module = pythonInstance.getModule("paddle_ocr")
        ocrQueue = Collections.synchronizedList(LinkedList())

        startJobProcessor()
    }

    fun enqueuePaddleTextDetection(bitmap: Bitmap, language: String?, callback: (List<Pair<BoundingBox, String>>?) -> Unit) {
        val genericCallback: (Any?) -> Unit = { result ->
            if (result is List<*>) {
                @Suppress("UNCHECKED_CAST")
                callback(result as List<Pair<BoundingBox, String>>?)
            } else {
                callback(null)
            }
        }
        synchronized(ocrQueue) {
            ocrQueue.add(OCRJob(bitmap, language, genericCallback, isRecognition = false))
            (ocrQueue as Any).notify()  // Notify any waiting threads that an item has been added
        }
    }

    fun enqueuePaddleTextRecognition(bitmap: Bitmap, language: String?, callback: (String?) -> Unit) {
        val genericCallback: (Any?) -> Unit = { result ->
            callback(result as String?)
        }
        synchronized(ocrQueue) {
            ocrQueue.add(OCRJob(bitmap, language, genericCallback, isRecognition = true))
            (ocrQueue as Any).notify()  // Notify any waiting threads that an item has been added
        }
    }

    private fun startJobProcessor() {
        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                // Get the next job in a synchronized manner
                val job = getNextJob()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        if (job.isRecognition) {
                            processRecognition(job)
                        } else {
                            processDetection(job)
                        }
                    } catch (e: Exception) {
                        Log.d("PaddleOCR Error", "Error processing OCR job: ${e.message}")
                    }
                }
            }
        }
    }

    private fun getNextJob(): OCRJob {
        synchronized(ocrQueue) {
            while (ocrQueue.isEmpty()) {
                (ocrQueue as Any).wait()  // Wait until there is an item in the queue
            }
            return ocrQueue.removeAt(0).also {
                if (ocrQueue.isNotEmpty()) {
                    (ocrQueue as Any).notify()  // Notify possibly waiting threads that the queue is still not empty
                }
            }
        }
    }

    private fun processDetection(job: OCRJob) {
        sendTextDetectionRequest(job.bitmap, job.language) { ocrResult ->
            val coordsWithText = resultToCoordinatesWithText(ocrResult.toString())
            val boundingBoxWithText = coordsWithText.map {
                parseBoundingBoxes(
                    it,
                    job.bitmap.width,
                    job.bitmap.height
                )
            }.filterNotNull()
            job.callback(boundingBoxWithText)
        }
    }

    private fun processRecognition(job: OCRJob) {
        sendTextRecognitionRequest(job.bitmap, job.language) { ocrResult ->
            job.callback(ocrResult)
        }
    }

    private fun sendTextRecognitionRequest(bitmap: Bitmap, language: String?, callback: (String?) -> Unit) {
        print("am in recog")
        val tempFile = createTempFile(context)
        FileOutputStream(tempFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        try {
            val result = module.callAttr(
                "enqueue_paddle_ocr_text_recognition",
                tempFile.absolutePath,
                language
            )
            print("result is out")

            callback(result.toString())
        } finally {
            tempFile.delete()
        }
    }

    private fun sendTextDetectionRequest(bitmap: Bitmap, language: String?, callback: (PyObject) -> Unit) {
        val tempFile = createTempFile(context)
        FileOutputStream(tempFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        try {
            val result = module.callAttr(
                "enqueue_paddle_ocr_text_detection",
                tempFile.absolutePath,
                language
            )
            callback(result)
        } finally {
            tempFile.delete()
        }
    }

    private fun resultToCoordinatesWithText(inputString: String): List<CoordinatesWithText> {
        val matches = regexPattern.findAll(inputString)

        return matches.map { match ->
            val coordinatesString = match.groups[1]?.value ?: ""
            val text = match.groups[2]?.value ?: ""
            val confidence = match.groups[3]?.value?.toDoubleOrNull() ?: 0.0

            // Parse coordinates
            val coordinatesPattern = """\[(\d+\.\d+), (\d+\.\d+)\]""".toRegex()
            val points = coordinatesPattern.findAll(coordinatesString).map { coordMatch ->
                listOf(coordMatch.groups[1]?.value?.toFloatOrNull() ?: 0f,
                    coordMatch.groups[2]?.value?.toFloatOrNull() ?: 0f)
            }.toList()

            CoordinatesWithText(points, TextResponse(text, confidence))
        }.toList()
    }

    private fun createTempFile(context: Context): File {
        val file = File(context.cacheDir, "ocr_image_${System.currentTimeMillis()}.png")
        return file
    }

    private fun parseBoundingBoxes(coordsWithText: CoordinatesWithText, width: Int, height: Int): Pair<BoundingBox, String>? {
        val points = coordsWithText.points
        val text = coordsWithText.texts.text

        // Extract all x and y values separately and coerce them within image bounds
        val xValues = points.map { it[0].coerceIn(0f, width.toFloat()) }
        val yValues = points.map { it[1].coerceIn(0f, height.toFloat()) }

        // Find minimum and maximum values from the coerced lists
        val minX = xValues.minOrNull() ?: return null
        val maxX = xValues.maxOrNull() ?: return null
        val minY = yValues.minOrNull() ?: return null
        val maxY = yValues.maxOrNull() ?: return null

        // Check if bounding box has non-zero width and height
        if (maxX > minX && maxY > minY) {
            return Pair(BoundingBox(minX, minY, maxX, maxY), text)
        } else {
            return null // Or handle according to your needs
        }
    }
}


/*

Sample Response

("/private/var/folders/jl/2_fhdcss6fx1snl95qh722_40000gn/T/gradio/5a057897c1d237c79a1cee5932c7ec5d32a621db/result.jpg",
 "[
     [
         [[40.0, 14.0], [165.0, 14.0], [165.0, 35.0], [40.0, 35.0]],
         ('亦資人昵?「', 0.6565303206443787)
     ],
     [
         [[157.0, 14.0], [229.0, 14.0], [229.0, 36.0], [157.0, 36.0]],
         ('剛オ我', 0.9743008017539978)
     ],
     [
         [[39.0, 42.0], [212.0, 44.0], [212.0, 67.0], [39.0, 64.0]],
         ('回来就没看兄地', 0.9483866691589355)
     ],
     [
         [[39.0, 73.0], [241.0, 73.0], [241.0, 94.0], [39.0, 94.0]],
         ('地不干活在似什ム?', 0.9077878594398499)
     ]
 ]",
 "亦資人昵?「\n剛オ我\n回来就没看兄地\n地不干活在似什ム?")
 */