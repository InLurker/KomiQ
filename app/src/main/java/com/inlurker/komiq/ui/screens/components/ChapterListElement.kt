package com.inlurker.komiq.ui.screens.components

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inlurker.komiq.ui.screens.MangaChapterPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListElement(
    chapterPreview: MangaChapterPreview,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    val formattedUploadDate = remember {
        DateFormat.format("dd MMM yyyy", chapterPreview.uploadDate)
    }

    val chapterDetails = buildAnnotatedString {
        if (chapterPreview.volumeNumber != 0) {
            append("Vol ${chapterPreview.volumeNumber}. ")
        }
        append("Chapter ${chapterPreview.chapterNumber} - ")
        append(chapterPreview.chapterName.takeIf { it.isNotEmpty() } ?: "Unknown")
    }

    val chapterSourceInfo = buildAnnotatedString {
        append(formattedUploadDate)
        append(" · ")
        append(chapterPreview.scanlationGroup.takeIf { it.isNotEmpty() } ?: "Unknown")
    }

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor),
            modifier = Modifier
                .wrapContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = chapterDetails,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = chapterSourceInfo,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 10.sp,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}