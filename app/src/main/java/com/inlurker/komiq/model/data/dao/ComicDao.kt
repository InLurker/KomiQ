package com.inlurker.komiq.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inlurker.komiq.model.data.Comic
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {
    @Query("SELECT * FROM comic")
    fun getAllComics(): Flow<List<Comic>>

    @Query("SELECT * FROM comic WHERE id = :comicId")
    fun getComicById(comicId: String): Flow<Comic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComics(comics: List<Comic>)
}