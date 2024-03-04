package com.inlurker.komiq.ui.screens.components.AnimatedComponents

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RotatingComponent(
    isRotated: Boolean,
    durationMillis: Int = 300,
    delayMillis: Int = 0,
    component: @Composable (Modifier) -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) 180f else 0f,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = LinearOutSlowInEasing
        ), label = "Rotating Component Animation"
    )

    val rotationModifier = Modifier.rotate(rotationAngle)
    component(rotationModifier)
}

@Composable
fun RotatingIcon(
    isRotated: Boolean,
    imageVector: ImageVector,
    modifier: Modifier = Modifier, // Default value for modifier
    tint: Color = Color.Unspecified // Default value for tint
) {
    RotatingComponent(
        isRotated = isRotated,
    ) { rotationModifier ->
        Icon(
            imageVector = imageVector,
            contentDescription = "Expand more",
            modifier = modifier.then(rotationModifier),
            tint = tint // Apply the tint color
        )
    }
}

@Composable
fun RotatingIconButton(
    isRotated: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    RotatingComponent(
        isRotated = isRotated,
    ) { rotationModifier ->
        IconButton(
            onClick = {
                onClick(!isRotated)
            },
            modifier = modifier
                .size(24.dp)
                .background(Color.Transparent)
                .then(rotationModifier)
        ) {
            content()
        }
    }
}