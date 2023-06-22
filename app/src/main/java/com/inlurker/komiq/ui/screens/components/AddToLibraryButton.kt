package com.inlurker.komiq.ui.screens.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLibraryButton(
    vibrantColor: Color,
    tonalFilledColor: Color,
    isInLibrary: Boolean,
    onToggleAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = remember { Animatable(Color.Transparent) }

    var buttonText by remember { mutableStateOf("Add To Library") }

    LaunchedEffect(isInLibrary) {
        if (isInLibrary) {
            buttonText = "In Library"
            backgroundColor.animateTo(tonalFilledColor)
        } else {
            buttonText = "Add to Library"
            backgroundColor.animateTo(Color.Transparent)
        }
    }

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Button(
            onClick = { onToggleAction() },
            colors = ButtonDefaults.buttonColors(backgroundColor.value),
            contentPadding = PaddingValues(
                start = 6.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = 8.dp
            ),
            border = if (isInLibrary) null else BorderStroke(width = 0.3.dp, color = Color.Gray),
            modifier = modifier
                .defaultMinSize(
                    minHeight = 1.dp,
                    minWidth = 1.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Crossfade(targetState = isInLibrary) { isInLibrary ->
                    if (isInLibrary) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Filled Bookmark Symbol",
                            tint = vibrantColor,
                            modifier = Modifier.size(12.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "Outlined Bookmark Symbol",
                            tint = vibrantColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = buttonText,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 0.1.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .animateContentSize()
                )
            }
        }
    }
}