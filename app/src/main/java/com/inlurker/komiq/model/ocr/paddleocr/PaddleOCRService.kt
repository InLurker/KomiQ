
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PaddleOCRService(private val context: Context) {
    private val pythonInstance: Python
    private val module: PyObject

    init {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }
        pythonInstance = Python.getInstance()
        module = pythonInstance.getModule("paddle_ocr")
    }

    fun sendOCRRequest(bitmap: Bitmap, language: String?, callback: (String?) -> Unit) {
        // Save bitmap to a temporary file
        val tempFile = createTempFile(context)
        FileOutputStream(tempFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Perform OCR
        val result = module.callAttr("enqueuePaddleOCR", tempFile.absolutePath, language)
        callback(result.toString())

        // Delete the temporary file
        tempFile.delete()
    }

    private fun createTempFile(context: Context): File {
        return File(context.cacheDir, "ocr_image_${System.currentTimeMillis()}.png")
    }
}
