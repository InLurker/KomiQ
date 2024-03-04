package com.inlurker.komiq.ui.screens.helper.Formatters

import android.text.format.DateUtils
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun formatDate(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    val daysDifference = ChronoUnit.DAYS.between(dateTime, now)

    return if (daysDifference in 0..7) {
        val relativeTimeSpan = DateUtils.getRelativeTimeSpanString(
            dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        )
        relativeTimeSpan.toString()
    } else {
        val formattedDate = dateTime.format(DateTimeFormatter.ofPattern("d MMM y"))
        formattedDate
    }
}