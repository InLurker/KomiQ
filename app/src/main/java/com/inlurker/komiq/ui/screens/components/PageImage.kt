package com.inlurker.komiq.ui.screens.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest


@Composable
fun PageImage(
    context: Context,
    imageUrl: String,
    colorFilter: ColorFilter,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .build()


    var isLoading by remember { mutableStateOf(true) }

    Box {
        SubcomposeAsyncImage(
            model = imageRequest,
            contentDescription = "Comic Chapter Page",
            contentScale = contentScale,
            colorFilter = colorFilter,
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
            },
            modifier = modifier
                .zIndex(1f)
                .align(Alignment.Center)
        )

        if (isLoading)
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .zIndex(0f)
            )
    }
}