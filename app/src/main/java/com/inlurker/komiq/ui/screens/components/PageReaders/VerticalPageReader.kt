package com.inlurker.komiq.ui.screens.components.PageReaders

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.inlurker.komiq.ui.screens.components.PageImage
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DynamicPageReader(
    readingDirection: ReadingDirection,
    pagesUrl: List<String>,
    colorFilter: ColorFilter,
    pagerState: PagerState,
    context: Context
) {
    when (readingDirection) {
        ReadingDirection.LeftToRight, ReadingDirection.RightToLeft ->
            HorizontalPageReader(
                pagerState = pagerState,
                pagesUrl = pagesUrl,
                colorFilter = colorFilter,
                readingDirection = readingDirection,
                context = context
            )

        ReadingDirection.TopToBottom, ReadingDirection.BottomToTop ->
            VerticalPageReader(
                pagerState = pagerState,
                pagesUrl = pagesUrl,
                colorFilter = colorFilter,
                readingDirection = readingDirection,
                context = context
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalPageReader(
    pagerState: PagerState,
    pagesUrl: List<String>,
    colorFilter: ColorFilter,
    readingDirection: ReadingDirection,
    context: Context
) {
    VerticalPager(
        state = pagerState,
        reverseLayout = readingDirection == ReadingDirection.BottomToTop,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { page ->
        PageImage(
            context = context,
            imageUrl = pagesUrl[page],
            colorFilter = colorFilter,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}