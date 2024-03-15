package com.inlurker.komiq.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {
    @Query("SELECT * FROM comic")
    fun getAllComics(): Flow<List<Comic>>

    @Query("SELECT * FROM comic WHERE id = :comicId AND languageSetting = :language")
    fun getComicById(comicId: String, language: ComicLanguageSetting): Flow<Comic?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComic(comic: Comic)

    @Query("DELETE FROM comic WHERE id = :comicId AND languageSetting = :language")
    suspend fun deleteComicById(comicId: String, language: ComicLanguageSetting)
}