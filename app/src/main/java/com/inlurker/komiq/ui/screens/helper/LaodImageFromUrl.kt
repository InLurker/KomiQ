package com.inlurker.komiq.ui.screens.helper

import android.content.Context
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.inlurker.komiq.R

fun loadImageFromUrl(
    context: Context,
    comicId: String,
    fileName: String
): ImageRequest {
    val imageUrl = "https://uploads.mangadex.org/covers/$comicId/$fileName.512.jpg"
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .placeholder(R.drawable.cover_placeholder)
        .crossfade(true)
        .allowHardware(false)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .build()
}