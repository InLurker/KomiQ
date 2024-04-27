
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.inlurker.komiq.R
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PaddleOCRService(private val context: Context) {
    private val pythonInstance: Python
    private val module: PyObject

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
    }

    fun enqueuePaddleTextDetection(bitmap: Bitmap, language: String?, callback: (List<Pair<BoundingBox, String>>?) -> Unit) {
        sendTextDetectionRequest(bitmap, language) { ocrResult ->
            val result = ocrResult.asList()[1].toString()
            val coordsWithText = resultToCoordinatesWithText(result)
            val boundingBoxWithText = coordsWithText.map { parseBoundingBoxes(it, bitmap.width, bitmap.height) }.filterNotNull()
            callback(boundingBoxWithText)
        }
    }

    fun enqueuePaddleTextRecognition(bitmap: Bitmap, language: String?, callback: (String?) -> Unit) {
        sendTextrecognitionRequest(bitmap, language) { ocrResult ->
            ocrResult?.let { result ->
                callback(result)
            }
        }
    }

    private fun sendTextrecognitionRequest(bitmap: Bitmap, language: String?, callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val tempFile = createTempFile(context)
            FileOutputStream(tempFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val result =
                module.callAttr("enqueuePaddleOCR", tempFile.absolutePath, language).asList()
            callback(result[2].toString())
            tempFile.delete()
        }
    }

    private fun sendTextDetectionRequest(bitmap: Bitmap, language: String?, callback: (PyObject) -> Unit) {
        val tempFile = createTempFile(context)
        FileOutputStream(tempFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        val result = module.callAttr("enqueuePaddleOCR", tempFile.absolutePath, language)
        callback(result)

        tempFile.delete()
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
        Log.d("coords", points.toString())

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

// Usage example within Kotlin environment
@Preview
@Composable
fun OCRApp() {
    val coroutineScope = rememberCoroutineScope()
    var boundingBoxResult by remember { mutableStateOf<String?>(null) }
    var textOnlyResult by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val bitmap = remember { BitmapFactory.decodeResource(context.resources, R.drawable.sample) }  // Ensure sample.png is in drawable folder

    val paddleOCRService = PaddleOCRService(LocalContext.current)

    Column(modifier = Modifier.padding(16.dp)) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Loaded Image",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    paddleOCRService.enqueuePaddleTextDetection(bitmap, "japan") { results ->
                        boundingBoxResult = results?.joinToString(separator = "\n") {
                            "Box: (${it.first.X1}, ${it.first.Y1}), (${it.first.X2}, ${it.first.Y2}) - Text: ${it.second}"
                        } ?: "Failed to detect text"
                    }
                }
            },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Detect Text BoundingBoxes")
        }
        Text("OCR Bounding Box Results: ${boundingBoxResult ?: "No result yet"}", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    paddleOCRService.enqueuePaddleTextRecognition(bitmap, "japan") { text ->
                        textOnlyResult = text ?: "Failed to recognize text"
                    }
                }
            },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Perform OCR Text Recognition")
        }
        Text("OCR Text Only Results: ${textOnlyResult ?: "No result yet"}")
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