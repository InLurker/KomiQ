package com.inlurker.komiq.model.data.repository

import com.inlurker.komiq.model.data.Comic


object ComicRepository {

    private val comics: MutableMap<String, Comic> = mutableMapOf()

    fun addComic(comic: Comic) {
        comics[comic.id] = comic
    }

    fun getComic(comicId: String): Comic? {
        return comics[comicId]
    }
}