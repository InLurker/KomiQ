package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.data.repository.ComicRepository.getComic
import kotlinx.coroutines.runBlocking




/*

fun main () {
    val search =
        ComicSearchQuery.Builder()
            .comicAmount(30)
            .offsetAmount((1 - 1) * 30)
            .build()
    runBlocking {
        val search2 = search.copy(offsetAmount = (2-1) * 30)
        println(search2.toUrlString())
        println(getComicList(search2))
    }
}
 */
fun main () {
    runBlocking {
        val manga = getComic("e1e38166-20e4-4468-9370-187f985c550e")
        println(manga)
    }
}
