package com.inlurker.komiq.model.data.mangadexapi.constants

object MangaStatus {
    const val ONGOING = "ongoing"
    const val COMPLETED = "completed"
    const val HIATUS = "hiatus"
    const val CANCELLED = "cancelled"
}

object ContentRating {
    const val SAFE = "safe"
    const val SUGGESTIVE = "suggestive"
    const val EROTICA = "erotica"
    const val PORNOGRAPHIC = "pornographic"
}

object MangaOrderOptions {
    const val TITLE = "title"
    const val YEAR = "year"
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val LATEST_UPLOADED_CHAPTER = "latestUploadedChapter"
    const val FOLLOWED_COUNT = "followedCount"
    const val RELEVANCE = "relevance"
}

object ChapterOrderOptions {
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val PUBLISH_AT = "publishAt"
    const val READABLE_AT = "readableAt"
    const val VOLUME = "volume"
    const val CHAPTER = "chapter"
}

object SortingOrder {
    const val ASC = "asc"
    const val DESC = "desc"
}