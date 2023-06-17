package com.inlurker.komiq.ui.screens.helper

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoadImageFromUrl(imageUrl: String, onImageLoaded: (Bitmap) -> Unit) {
    val context = LocalContext.current
    val imageLoader = remember { ImageLoader(context) }

    LaunchedEffect(imageUrl) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = withContext(Dispatchers.IO) {
            imageLoader.execute(request).drawable?.toBitmap()
        }

        result?.let {
            onImageLoaded(it)
        }
    }
}