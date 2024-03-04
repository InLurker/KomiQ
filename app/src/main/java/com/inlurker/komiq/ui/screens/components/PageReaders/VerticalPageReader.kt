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
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPages
import com.inlurker.komiq.ui.screens.components.PageImage
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl
import com.inlurker.komiq.ui.screens.helper.ReadingDirection

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalPageReader(
    pagerState: PagerState,
    chapterPages: ChapterPages?,
    colorFilter: ColorFilter,
    readingDirection: ReadingDirection,
    context: Context
) {
    VerticalPager(
        state = pagerState,
        reverseLayout = readingDirection == ReadingDirection.BottomToTop,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { page ->
        chapterPages?.data?.get(page)?.let { filename ->
            val imageUrl = getChapterPageImageUrl(chapterPages.hash, filename)
            PageImage(
                context = context,
                imageUrl = imageUrl,
                colorFilter = colorFilter,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
            )
        }
    }
}