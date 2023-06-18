package com.inlurker.komiq.ui.screens.helper

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoadImageFromUrl(context: Context, comicId: String, fileName: String, onImageLoaded: (Bitmap) -> Unit) {

    val imageUrl = "https://uploads.mangadex.org/covers/$comicId/$fileName.512.jpg"
    val imageLoader = remember { ImageLoader(context) }
    LaunchedEffect(imageUrl) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .build()

        val result = withContext(Dispatchers.IO) {
            imageLoader.execute(request).drawable?.toBitmap()
        }
        println(imageUrl)
        result?.let {
            onImageLoaded(it)
        }
    }
}