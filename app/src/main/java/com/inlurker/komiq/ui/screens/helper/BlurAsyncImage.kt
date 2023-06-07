package com.inlurker.komiq.ui.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.commit451.coiltransformations.BlurTransformation

@Composable
fun BlurredAsyncImage(
    context: Context,
    imageUrl: String,
    blurRadius: Float,
    sampling: Float = 1.0f,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
): @Composable () -> Unit {
    val blurredImagePainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
            .apply(block = fun ImageRequest.Builder.() {
                transformations(
                    BlurTransformation(
                        context,
                        blurRadius,
                        sampling
                    )
                )
            }).build()
    )

    return {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(blurredImagePainter)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}
