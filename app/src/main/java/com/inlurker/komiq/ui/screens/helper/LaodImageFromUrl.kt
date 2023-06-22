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
    val imageUrl = getComicCoverUrl(
        comicId = comicId,
        fileName = fileName
    )
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .placeholder(R.drawable.cover_placeholder)
        .crossfade(true)
        .allowHardware(false)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .build()
}