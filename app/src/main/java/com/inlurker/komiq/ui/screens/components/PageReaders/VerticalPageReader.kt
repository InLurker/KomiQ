package com.inlurker.komiq.ui.screens.components.PageReaders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalPageReader(
    pagerState: PagerState,
    readingDirection: ReadingDirection,
    content: @Composable (Int) -> Unit
) {
    VerticalPager(
        state = pagerState,
        reverseLayout = readingDirection == ReadingDirection.BottomToTop,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { page ->
        content(page)
    }
}

