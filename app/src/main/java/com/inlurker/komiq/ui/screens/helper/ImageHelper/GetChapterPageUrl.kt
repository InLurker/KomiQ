package com.inlurker.komiq.ui.screens.helper.ImageHelper

fun getChapterPageImageUrl(hashed: String, filename: String): String {
    return "https://uploads.mangadex.org/data/$hashed/$filename"
}