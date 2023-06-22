package com.inlurker.komiq.model.mangadexapi.helper

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CompletableDeferred
import okhttp3.Request

suspend fun <T> performMangadexApiRequest(
    request: Request,
    adapter: JsonAdapter<T>
): T? {
    val deferred = CompletableDeferred<T?>()
    MangaDexApiHelper.enqueueRequest(request) { response ->
        val json = response?.body?.string()
        println(json ?: "null")
        val parsedResult = if (json != null) {
            adapter.fromJson(json)
        } else {
            null
        }
        deferred.complete(parsedResult)
    }
    return deferred.await()
}
