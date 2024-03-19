package com.inlurker.komiq.ui.screens.helper.Formatters

import androidx.compose.ui.text.buildAnnotatedString
import java.time.LocalDateTime

fun formatChapterVolume(volumeNumber: Float, chapterNumber: Float, chapterName: String? = null): String {
    return buildAnnotatedString {
        if (volumeNumber != 0f) {
            append("Vol. ${removeTrailingZero(volumeNumber)} ")
        }
        val chapterText = "Chapter ${removeTrailingZero(chapterNumber)}"
        append(chapterText)
        chapterName?.let { name ->
            if (name.isNotEmpty() && name != chapterText) {
                append(" - ")
                append(chapterName.takeIf { it.isNotEmpty() } ?: "Unknown")
            }
        }
    }.toString()
}

fun formatChapterSourceInfo(uploadDate: LocalDateTime, scanlationGroup: String): String {
    return buildAnnotatedString {
        append("${formatDate(uploadDate)} • ")
        append(scanlationGroup.takeIf { it.isNotEmpty() } ?: "Unknown")
    }.toString()
}