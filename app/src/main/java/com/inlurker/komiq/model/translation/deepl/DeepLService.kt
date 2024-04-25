package com.inlurker.komiq.model.translation.deepl

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

object DeeplTranslateService {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val jsonAdapter = moshi.adapter(TranslationRequest::class.java)

    data class TranslationRequest(val text: String, val source_lang: String, val target_lang: String)

    fun enqueueTranslateRequest(text: String, sourceLang: String, targetLang: String, callback: (String?) -> Unit) {
        val translationRequest = TranslationRequest(text, sourceLang, targetLang)
        val requestBody = jsonAdapter.toJson(translationRequest).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://api.deeplx.org/translate")
            .post(requestBody)
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val json = responseBody.string()
                        val jsonObject = moshi.adapter(Map::class.java).fromJson(json)
                        val textData = jsonObject?.get("data") as? String
                        callback(textData)
                    }
                } else {
                    callback(null)
                }
            }
        })
    }
}