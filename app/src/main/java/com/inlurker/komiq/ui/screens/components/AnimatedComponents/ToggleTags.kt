package com.inlurker.komiq.ui.screens.components.AnimatedComponents

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inlurker.komiq.model.data.mangadexapi.constants.GenreTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleTags(
    label: String,
    isSelected: Boolean,
    onStateChange: (Boolean) -> Unit
) {
    val tonalBackground = MaterialTheme.colorScheme.secondaryContainer
    val outlinedBackground =  MaterialTheme.colorScheme.surface
    val backgroundColor = remember {
        Animatable(
            if (isSelected) {
                tonalBackground
            } else {
                outlinedBackground
            }
        )
    }

    val onTonalTextColor = MaterialTheme.colorScheme.onSecondaryContainer
    val onOutlinedTextColor =  MaterialTheme.colorScheme.primary
    val textColor = remember {
        Animatable(
            if (isSelected) {
                onTonalTextColor
            } else {
                onOutlinedTextColor
            }
        )
    }
    val outlineColor = MaterialTheme.colorScheme.outline
    val borderWidth by animateFloatAsState(if (isSelected) 0f else 1f, label = "Outline Animation")

    LaunchedEffect(isSelected) {
        if (isSelected) {
            backgroundColor.animateTo(tonalBackground)
            textColor.animateTo(onTonalTextColor)
        } else {
            backgroundColor.animateTo(outlinedBackground)
            textColor.animateTo(onOutlinedTextColor)
        }
    }

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Button(
            onClick = { onStateChange(!isSelected) },
            colors = ButtonDefaults.buttonColors(backgroundColor.value),
            border = if (isSelected) null else BorderStroke(
                width = borderWidth.dp,
                color = outlineColor
            ),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 8.dp,
                bottom = 8.dp,
                end = 12.dp
            ),
            modifier = Modifier
                .defaultMinSize(
                    minHeight = 1.dp,
                    minWidth = 1.dp
                )
        ) {
            Text(
                text = label,
                color = textColor.value,
                fontSize = 13.sp,
                lineHeight = 10.sp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun ToggleTags_Preview() {
    Column {
        GenreTag.asList().forEach { genre ->
            var isButtonSelected by remember { mutableStateOf(false) }
            ToggleTags(
                label = genre.description,
                isSelected = isButtonSelected,
                onStateChange = { changeResult ->
                    isButtonSelected = changeResult
                }
            )
        }
    }
}