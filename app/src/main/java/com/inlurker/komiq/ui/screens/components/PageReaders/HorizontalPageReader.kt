package com.inlurker.komiq.ui.screens.components.PageReaders

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.inlurker.komiq.ui.screens.components.PageImage
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPageReader(
    pagerState: PagerState,
    pagesUrl: List<String>,
    colorFilter: ColorFilter,
    readingDirection: ReadingDirection,
    context: Context
) {
    HorizontalPager(
        state = pagerState,
        reverseLayout = readingDirection == ReadingDirection.RightToLeft,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        PageImage(
            context = context,
            imageUrl = pagesUrl[page],
            colorFilter = colorFilter,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
