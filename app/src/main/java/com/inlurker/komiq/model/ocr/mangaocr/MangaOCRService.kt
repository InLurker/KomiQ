package com.inlurker.komiq.model.ocr.mangaocr


import android.graphics.Bitmap
import android.util.Log
import com.inlurker.komiq.model.ocr.helper.bitmapToRequestBody
import com.squareup.moshi.JsonReader
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.util.concurrent.TimeUnit

object MangaOCRService {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    fun enqueueOCRRequest(bitmap: Bitmap, apiKey: String, callback: (String?) -> Unit) {
        val requestBody = bitmapToRequestBody(bitmap)
        val request = Request.Builder()
            .url("https://api-inference.huggingface.co/models/kha-white/manga-ocr-base")
            .post(requestBody)
            .header("Authorization", "Bearer $apiKey")
            .build()

        // Use OkHttpClient's enqueue to handle concurrency automatically
        sendRequest(request, callback)
    }

    private fun sendRequest(request: Request, callback: (String?) -> Unit) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
                Log.d("MangaOCR", "Error: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        if (response.isSuccessful) {
                            val result = parseResult(response.body?.string().orEmpty())
                            callback(result)
                        } else {
                            callback(null)
                            Log.d("MangaOCR", "Unexpected: $response")
                        }
                    } catch (e: Exception) {
                        callback(null)
                        Log.d("MangaOCR", "Exception: $e")
                    }
                }
            }
        })
    }

    private fun parseResult(json: String): String? {
        val buffer = Buffer().writeUtf8(json)
        val reader = JsonReader.of(buffer)
        var result: String? = null

        reader.beginArray() // Start reading the array
        if (reader.hasNext()) {
            reader.beginObject() // Start reading the first object
            while (reader.hasNext()) {
                if (reader.nextName() == "generated_text") {
                    result = reader.nextString() // Get the value of 'generated_text'
                    break
                } else {
                    reader.skipValue() // Skip any other values
                }
            }
            reader.endObject() // End reading the first object
        }
        reader.endArray() // End reading the array

        reader.close() // Close the reader
        return result
    }
}


/*
Sample Request

curl -X POST https://api-inference.huggingface.co/models/kha-white/manga-ocr-base \
-H "Authorization: Bearer YOUR_API_KEY" \
-H "Content-Type: application/octet-stream" \
--data-binary @sample.png

 */