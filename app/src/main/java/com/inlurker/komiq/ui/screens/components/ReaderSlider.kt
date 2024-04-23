package com.inlurker.komiq.ui.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReaderSlider(
    bottomAppBarVisible: Boolean, // Controls the visibility of the bottom app bar or slider
    sliderDirection: LayoutDirection, // Determines the layout direction of the slider (LTR or RTL)
    pagerState: PagerState,
    modifier: Modifier
) {
    val totalPages = pagerState.pageCount

    var tempSliderValue by remember {
        mutableIntStateOf(pagerState.currentPage)
    }

    var scrollPageReader by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(scrollPageReader) {
        if (scrollPageReader) {
            pagerState.animateScrollToPage(tempSliderValue)
            scrollPageReader = false
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (!scrollPageReader) { //if currentPage change not because of the scrolling
            tempSliderValue = pagerState.currentPage
        }
    }

    AnimatedVisibility(
        visible = bottomAppBarVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides sliderDirection) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = (tempSliderValue + 1).toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(30.dp)
                )
                Slider(
                    value = tempSliderValue.toFloat(),
                    onValueChange = { tempSliderValue = it.toInt() },
                    onValueChangeFinished = {
                        scrollPageReader = true
                    },
                    valueRange = 0f..(if (totalPages > 0) totalPages - 1 else 0).toFloat(),
                    steps = if (totalPages > 2) totalPages - 2 else 0,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = (totalPages).toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(30.dp)
                )
            }
        }
    }
}