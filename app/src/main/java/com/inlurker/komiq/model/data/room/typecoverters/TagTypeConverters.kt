package com.inlurker.komiq.model.data.room.typecoverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inlurker.komiq.model.data.datamodel.Tag

class TagTypeConverter {
    @TypeConverter
    fun fromTagsList(tags: List<Tag>): String {
        val gson = Gson()
        return gson.toJson(tags)
    }

    @TypeConverter
    fun toTagsList(tagsString: String): List<Tag> {
        val gson = Gson()
        val type = object : TypeToken<List<Tag>>() {}.type
        return gson.fromJson(tagsString, type)
    }
}