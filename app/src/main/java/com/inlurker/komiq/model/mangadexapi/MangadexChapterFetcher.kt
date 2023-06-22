package com.inlurker.komiq.model.mangadexapi

import com.inlurker.komiq.model.data.Chapter
import com.inlurker.komiq.model.data.repository.ComicRepository.getComicChapterList
import kotlinx.coroutines.runBlocking


/*
get manga chapter list https://api.mangadex.org/manga/$comicid/feed?translatedLanguage[]=en
example url: https://api.mangadex.org/manga/a3f91d0b-02f5-4a3d-a2d0-f0bde7152370/feed?translatedLanguage[]=en
*/



// get chapter pages (filenames) https://api.mangadex.org/at-home/server/$comicId
// example url: https://api.mangadex.org/at-home/server/a1bd9359-c160-4fb5-acfe-3f0423441841



// page image https://uploads.mangadex.org/data/$hashed/$filename
// example url: https://uploads.mangadex.org/data/3303dd03ac8d27452cce3f2a882e94b2/1-f7a76de10d346de7ba01786762ebbedc666b412ad0d4b73baa330a2a392dbcdd.png
//


fun main() {
    val comicId = "d7037b2a-874a-4360-8a7b-07f2899152fd"
    val chapterList: List<Chapter>
    runBlocking {
        chapterList = getComicChapterList(comicId)
    }
    for ((index, chapter) in chapterList.withIndex()) {
        println(index)
        println("Vol ${chapter.volume} Ch. ${chapter.chapter}")
        println("Chapter ID: ${chapter.id}")
        println("Title: ${chapter.title}")
        println("Scan: ${chapter.scanlationGroup}")
        println()
    }
}