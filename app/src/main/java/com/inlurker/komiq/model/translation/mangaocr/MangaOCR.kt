package com.inlurker.komiq.model.translation.mangaocr


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.JsonParser
import com.inlurker.komiq.BuildConfig
import com.inlurker.komiq.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

object MangaOCRService {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    private val requestQueue: MutableList<QueuedRequest> = mutableListOf()

    fun enqueueOCRRequest(bitmap: Bitmap, apiKey: String, callback: (String?) -> Unit) {
        synchronized(requestQueue) {
            val requestBody = bitmapToRequestBody(bitmap)
            val request = Request.Builder()
                .url("https://api-inference.huggingface.co/models/kha-white/manga-ocr-base")
                .post(requestBody)
                .header("Authorization", "Bearer $apiKey")
                .build()

            val queuedRequest = QueuedRequest(request) { response ->
                val result = response?.body?.string()?.let { parseResult(it) }
                callback(result)
            }
            requestQueue.add(queuedRequest)
            processRequestQueue()
        }
    }

    private fun processRequestQueue() {
        synchronized(requestQueue) {
            if (requestQueue.isNotEmpty()) {
                val queuedRequest = requestQueue.first()
                if (!queuedRequest.isRunning) {
                    queuedRequest.isRunning = true
                    sendRequest(queuedRequest)
                }
            }
        }
    }

    private fun sendRequest(queuedRequest: QueuedRequest) {
        client.newCall(queuedRequest.request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                queuedRequest.callback(null)
                handleRequestCompletion(queuedRequest)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        queuedRequest.callback(response)
                    } else {
                        queuedRequest.callback(null)
                    }
                } finally {
                    response.close()
                    handleRequestCompletion(queuedRequest)
                }
            }
        })
    }

    private fun handleRequestCompletion(queuedRequest: QueuedRequest) {
        synchronized(requestQueue) {
            queuedRequest.isRunning = false
            requestQueue.remove(queuedRequest)
            processRequestQueue()
        }
    }

    private fun bitmapToRequestBody(bitmap: Bitmap): RequestBody {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return byteArray.toRequestBody("application/octet-stream".toMediaTypeOrNull())
    }

    private fun parseResult(json: String): String? {
        // Assuming JSON format: [{"generated_text":"text"}]
        return JsonParser.parseString(json).asJsonArray.firstOrNull()?.asJsonObject?.get("generated_text")?.asString
    }

    private class QueuedRequest(val request: Request, val callback: (Response?) -> Unit) {
        var isRunning: Boolean = false
    }
}

class MangaOcrApiManagerViewModel : ViewModel() {

    private val apiKey = BuildConfig.HUGGINGFACE_API_TOKEN  // Use your actual Hugging Face API key

    fun processImage(bitmap: Bitmap, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { // Use Dispatchers.IO for network operations
            MangaOCRService.enqueueOCRRequest(bitmap, apiKey) { result ->
                callback(result)
            }
        }
    }
}

@Preview
@Composable
fun OCRResultDisplayWithButton() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var ocrResult by remember { mutableStateOf<String?>(null) }
    val mangaOcrApiManager = viewModel<MangaOcrApiManagerViewModel>() // Access the ViewModel
    var text by remember { mutableStateOf("Click the button to process the image.") }

    Column {
        Text(text = BuildConfig.HUGGINGFACE_API_TOKEN)
        Button(onClick = {
            text = "Processing..."
            scope.launch {
                val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.sample) // Replace 'sample' with your actual drawable's name
                mangaOcrApiManager.processImage(bitmap) { result ->
                    ocrResult = result
                }
            }
        }) {
            Text("Process Image")
        }

        // Display the OCR result or a default message
        Text(text = ocrResult ?: text)
    }
}