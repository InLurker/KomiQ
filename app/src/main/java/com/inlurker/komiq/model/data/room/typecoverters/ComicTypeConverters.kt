package com.inlurker.komiq.model.data.room.typecoverters

import androidx.room.TypeConverter

class ComicTypeConverters {
    @TypeConverter
    fun fromAuthorsList(authors: List<String>): String {
        return authors.joinToString(";")
    }

    @TypeConverter
    fun toAuthorsList(authorsString: String): List<String> {
        return authorsString.split(";")
    }
}