package com.inlurker.komiq.model.mangadexapi.helper

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

object MangaDexApiHelper {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val requestQueue: MutableList<Request> = mutableListOf()

    private var isRequestRunning: Boolean = false

    fun enqueueRequest(request: Request, callback: (Response?) -> Unit) {
        synchronized(requestQueue) {
            requestQueue.add(request)
            processRequestQueue(callback)
        }
    }

    private fun processRequestQueue(callback: (Response?) -> Unit) {
        if (!isRequestRunning && requestQueue.isNotEmpty()) {
            val request = requestQueue.removeAt(0)
            isRequestRunning = true
            sendRequest(request, callback)
        }
    }

    private fun sendRequest(request: Request, callback: (Response?) -> Unit) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                // Handle request failure
                isRequestRunning = false
                processRequestQueue(callback)
            }
            override fun onResponse(call: Call, response: Response) {
                // Handle request response
                isRequestRunning = false
                callback(response)
                Thread.sleep(200) // Add a delay of 200 milliseconds (1/5th of a second)
                processRequestQueue(callback)
            }
        })
    }
}