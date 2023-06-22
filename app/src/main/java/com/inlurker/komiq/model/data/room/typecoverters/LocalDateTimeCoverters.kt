package com.inlurker.komiq.model.data.room.typecoverters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeConverter {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(localDateTimeString: String?): LocalDateTime? {
        return localDateTimeString?.let { LocalDateTime.parse(it, formatter) }
    }
}