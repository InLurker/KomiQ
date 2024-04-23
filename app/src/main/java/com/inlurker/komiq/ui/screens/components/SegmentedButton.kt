package com.inlurker.komiq.ui.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedButton(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
            .height(40.dp)
    ) {
        content()
    }
}

@Composable
fun SegmentedButtonItem(
    isSelected: Boolean = false,
    onClick: (Boolean) -> Unit,
    selectedColor: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    unselectedColor: ButtonColors = ButtonDefaults.textButtonColors(),
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val buttonColor by remember(isSelected) {
        mutableStateOf(
            if (isSelected) selectedColor else unselectedColor
        )
    }

    // Expand Animation
    val expandTransition = remember {
        expandHorizontally(
            expandFrom = Alignment.Start,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Collapse Animation
    val collapseTransition = remember {
        shrinkHorizontally(
            shrinkTowards = Alignment.Start,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    TextButton(
        onClick = { onClick(!isSelected) },
        colors = buttonColor,
        shape = RectangleShape,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(
                visible = isSelected,
                enter = expandTransition,
                exit = collapseTransition
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check Icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(vertical = 4.dp)
                )
            }
            content()
        }
    }
}

@Preview
@Composable
fun SegmentedControlPreview() {
    var isGreyscaleSelected by remember { mutableStateOf(false) }
    var isInvertSelected by remember { mutableStateOf(false) }

    SegmentedButton {
        SegmentedButtonItem(
            isSelected = isGreyscaleSelected,
            onClick = { select ->
                isGreyscaleSelected = select
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Greyscale")
        }
        Divider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxHeight()  //fill the max height
                .width(1.dp)
        )
        SegmentedButtonItem(
            isSelected = isInvertSelected,
            onClick = { select ->
                isInvertSelected = select
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Invert")
        }
    }
}