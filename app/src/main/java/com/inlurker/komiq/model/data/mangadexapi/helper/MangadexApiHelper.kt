package com.inlurker.komiq.model.data.mangadexapi.helper

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

object MangaDexApiHelper {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val requestQueue: MutableList<QueuedRequest> = mutableListOf()

    fun enqueueRequest(request: Request, callback: (Response?) -> Unit) {
        synchronized(requestQueue) {
            val queuedRequest = QueuedRequest(request, callback)
            requestQueue.add(queuedRequest)
            processRequestQueue()
        }
    }

    private fun processRequestQueue() {
        synchronized(requestQueue) {
            if (requestQueue.isNotEmpty()) {
                val queuedRequest = requestQueue[0]
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
                // Handle request failure
                handleRequestCompletion(queuedRequest)
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle request response
                queuedRequest.callback(response)
                Thread.sleep(200) // Add a delay of 1/5th of a second between requests
                handleRequestCompletion(queuedRequest)
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

    private class QueuedRequest(val request: Request, val callback: (Response?) -> Unit) {
        var isRunning: Boolean = false
    }
}