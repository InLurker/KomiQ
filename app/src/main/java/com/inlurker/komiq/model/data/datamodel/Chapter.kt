package com.inlurker.komiq.model.data.datamodel


import java.time.LocalDateTime


data class Chapter(
    val id: String,
    val volume: Float?,
    val chapter: Float,
    val title: String,
    val publishAt: LocalDateTime,
    val pages: Int,
    val scanlationGroup: String,
    val url: String?
)