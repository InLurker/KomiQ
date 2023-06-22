package com.inlurker.komiq.model.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inlurker.komiq.model.data.dao.ComicDao
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.room.typecoverters.ComicTypeConverters
import com.inlurker.komiq.model.data.room.typecoverters.LocalDateTimeTypeConverter
import com.inlurker.komiq.model.data.room.typecoverters.TagTypeConverter

@Database(entities = [Comic::class], version = 1)
@TypeConverters(
    ComicTypeConverters::class,
    TagTypeConverter::class,
    LocalDateTimeTypeConverter::class
)
abstract class ComicDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao

    companion object {
        @Volatile
        private var INSTANCE: ComicDatabase? = null

        fun getDatabase(context: Context): ComicDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ComicDatabase::class.java,
                    "comic_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}