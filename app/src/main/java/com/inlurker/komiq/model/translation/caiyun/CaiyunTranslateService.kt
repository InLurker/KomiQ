package com.inlurker.komiq.model.translation.caiyun

import com.inlurker.komiq.BuildConfig
import com.inlurker.komiq.model.translation.helper.QueuedRequest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object CaiyunTranslateService {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    val apiKey = BuildConfig.CAIYUN_API_TOKEN

    private val requestQueue: MutableList<QueuedRequest> = mutableListOf()

    fun enqueueTranslateRequest(text: String, transType: String, callback: (String?) -> Unit) {
        synchronized(requestQueue) {
            val requestBody = buildRequestBody(text, transType)
            val request = Request.Builder()
                .url("https://api.interpreter.caiyunai.com/v1/translator")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-authorization", "token $apiKey")
                .build()

            val queuedRequest = QueuedRequest(request) { response ->
                val result = response?.body?.string()?.let { parseResult(it) }
                callback(result)
            }
            requestQueue.add(queuedRequest)
            processRequestQueue()
        }
    }

    private fun buildRequestBody(text: String, transType: String): RequestBody {
        val json = JSONObject().apply {
            put("source", JSONArray().put(text))
            put("trans_type", transType)
            put("request_id", "demo")
            put("detect", true)
        }
        return json.toString().toRequestBody("application/json".toMediaTypeOrNull())
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

    private fun parseResult(json: String?): String? {
        if (json == null) return null

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
        val adapter: JsonAdapter<Map<String, Any>> = moshi.adapter(type)

        try {
            val result = adapter.fromJson(json)
            if (result != null && result.containsKey("target")) {
                val translations = result["target"] as? List<*>
                return translations?.firstOrNull()?.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

/*
Example request

curl -X POST http://api.interpreter.caiyunai.com/v1/translator \
-H "Content-Type: application/json" \
-H "x-authorization: token YOUR_TOKEN" \
-d '{"source": ["Lingocloud is the best translation service."], "trans_type": "auto2zh", "request_id": "demo", "detect": true}'

Response

{"target":["\u5f69\u4e91\u5c0f\u8bd1\u662f\u6700\u597d\u7684\u7ffb\u8bd1\u670d\u52a1\u3002"],"rc":0,"confidence":0.8,"trans_type":"ja2zh"}

 */