package com.inlurker.komiq.ui.screens.components.PageReaders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DynamicPageReader(
    readingDirection: ReadingDirection,
    pagerState: PagerState,
    content: @Composable (Int) -> Unit
) {
    when (readingDirection) {
        ReadingDirection.LeftToRight, ReadingDirection.RightToLeft ->
            HorizontalPageReader(
                pagerState = pagerState,
                readingDirection = readingDirection,
                content = content
            )

        ReadingDirection.TopToBottom, ReadingDirection.BottomToTop ->
            VerticalPageReader(
                pagerState = pagerState,
                readingDirection = readingDirection,
                content = content
            )
    }
}