package com.inlurker.komiq.ui.screens.helper

import android.text.format.DateUtils
import java.time.LocalDateTime
import java.time.ZoneOffset

fun formatDate(dateTime: LocalDateTime): String {
    val epochMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    val relativeTimeSpan = DateUtils.getRelativeTimeSpanString(epochMillis)
    return relativeTimeSpan.toString()
}