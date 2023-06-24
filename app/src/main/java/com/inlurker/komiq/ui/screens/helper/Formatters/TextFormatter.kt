package com.inlurker.komiq.ui.screens.helper.Formatters

import androidx.compose.ui.text.buildAnnotatedString
import java.time.LocalDateTime

fun formatChapterVolume(volumeNumber: Int, chapterNumber: Float, chapterName: String? = null): String {
    return buildAnnotatedString {
        if (volumeNumber != 0) {
            append("Vol. $volumeNumber ")
        }
        append("Chapter ${removeTrailingZero(chapterNumber)}")
        if(chapterName != null) {
            append(" - ")
            append(chapterName.takeIf { it.isNotEmpty() } ?: "Unknown")
        }
    }.toString()
}

fun formatChapterSourceInfo(uploadDate: LocalDateTime, scanlationGroup: String): String {
    return buildAnnotatedString {
        append("${formatDate(uploadDate)} • ")
        append(scanlationGroup.takeIf { it.isNotEmpty() } ?: "Unknown")
    }.toString()
}