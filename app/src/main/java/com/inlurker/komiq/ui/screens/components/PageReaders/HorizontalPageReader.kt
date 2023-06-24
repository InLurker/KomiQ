package com.inlurker.komiq.ui.screens.components.PageReaders

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import com.inlurker.komiq.model.data.mangadexapi.adapters.ChapterPages
import com.inlurker.komiq.ui.screens.components.PageImage
import com.inlurker.komiq.ui.screens.helper.ReadingDirection
import com.inlurker.komiq.ui.screens.helper.ImageHelper.getChapterPageImageUrl


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPageReader(
    pagerState: PagerState,
    totalPages: Int,
    chapterPages: ChapterPages?,
    colorFilter: ColorFilter,
    readingDirection: ReadingDirection,
    context: Context
) {
    HorizontalPager(
        state = pagerState,
        pageCount = totalPages,
        reverseLayout = readingDirection == ReadingDirection.RightToLeft,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        chapterPages?.data?.get(page)?.let { filename ->
            val imageUrl = getChapterPageImageUrl(chapterPages.hash, filename)
            PageImage(
                imageUrl = imageUrl,
                colorFilter = colorFilter,
                context = context
            )
        }
    }
}
