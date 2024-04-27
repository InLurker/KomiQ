package com.inlurker.komiq.model.recognition.helper

import android.graphics.Bitmap
import com.inlurker.komiq.viewmodel.utils.bitmapToByteArray
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun bitmapToRequestBody(bitmap: Bitmap): RequestBody {
    val byteArray = bitmapToByteArray(bitmap)
    return byteArray.toRequestBody("application/octet-stream".toMediaTypeOrNull())
}