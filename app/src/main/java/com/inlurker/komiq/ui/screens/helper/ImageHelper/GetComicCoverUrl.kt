package com.inlurker.komiq.ui.screens.helper.ImageHelper

fun getComicCoverUrl(
    comicId: String,
    fileName: String,
): String = "https://uploads.mangadex.org/covers/$comicId/$fileName.512.jpg"