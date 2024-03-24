package com.inlurker.komiq.ui.screens.components.PageReaders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPageReader(
    pagerState: PagerState,
    readingDirection: ReadingDirection,
    content: @Composable (Int) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        reverseLayout = readingDirection == ReadingDirection.RightToLeft,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        content(page)
    }
}
