package com.inlurker.komiq.model.translation.mangaocr


import android.graphics.Bitmap
import com.google.gson.JsonParser
import com.inlurker.komiq.viewmodel.utils.bitmapToByteArray
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
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
        val byteArray = bitmapToByteArray(bitmap)
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
