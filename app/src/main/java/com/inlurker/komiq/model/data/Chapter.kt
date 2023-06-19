package com.inlurker.komiq.model.data


import java.time.LocalDateTime


data class Chapter(
    val id: String,
    val volume: Int?,
    val chapter: Float,
    val title: String,
    val publishAt: LocalDateTime,
    val pages: Int,
    val scanlationGroup: String
)