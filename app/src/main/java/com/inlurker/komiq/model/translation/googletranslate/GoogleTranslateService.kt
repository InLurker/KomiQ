package com.inlurker.komiq.model.translation.googletranslate

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object GoogleTranslateService {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val requestQueue: MutableList<QueuedRequest> = mutableListOf()

    fun enqueueTranslateRequest(text: String, sourceLang: String, targetLang: String, callback: (String?) -> Unit) {
        synchronized(requestQueue) {
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("translate.googleapis.com")
                .addPathSegment("translate_a")
                .addPathSegment("single")
                .addQueryParameter("client", "gtx")
                .addQueryParameter("sl", sourceLang)
                .addQueryParameter("tl", targetLang)
                .addQueryParameter("dt", "t")
                .addQueryParameter("q", text)
                .build()

            val request = Request.Builder()
                .url(url)
                .get()
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
        // Random delay between 25ms to 300ms
        val delay = Random.nextLong(25, 301)
        Thread.sleep(delay)

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

    fun parseResult(json: String): String? {
        val moshi = Moshi.Builder().build()

        // We define the type as a List of Any since we know there's mixed types at the top level.
        val type = Types.newParameterizedType(List::class.java, Any::class.java)
        val adapter: JsonAdapter<List<Any>> = moshi.adapter(type)

        try {
            // Parse the JSON string
            val result = adapter.fromJson(json)

            // Extract the specific translated part
            // We need to safely cast and navigate through the structure knowing that our target is deeply nested.
            if (result != null && result.size > 0) {
                val firstElement = result[0]
                if (firstElement is List<*>) {
                    val secondElement = firstElement[0]
                    if (secondElement is List<*>) {
                        return secondElement[0] as? String
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private class QueuedRequest(val request: Request, val callback: (Response?) -> Unit) {
        var isRunning: Boolean = false
    }
}

/*
Example request

curl "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=es&dt=t&q=Hello%20World"
 */