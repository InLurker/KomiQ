package com.inlurker.komiq.model.data.mangadexapi.constants

enum class MangaStatus(val status: String) {
    ONGOING("ongoing"),
    COMPLETED("completed"),
    HIATUS("hiatus"),
    CANCELLED("cancelled");

    companion object {
        fun asList(): List<MangaStatus> = values().toList()
    }
}

enum class ContentRating(val rating: String) {
    SAFE("safe"),
    SUGGESTIVE("suggestive"),
    EROTICA("erotica"),
    PORNOGRAPHIC("pornographic");

    companion object {
        fun asList(): List<ContentRating> = values().toList()
    }
}

enum class MangaOrderOptions(val option: String) {
    TITLE("title"),
    YEAR("year"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    LATEST_UPLOADED_CHAPTER("latestUploadedChapter"),
    FOLLOWED_COUNT("followedCount"),
    RELEVANCE("relevance");

    companion object {
        fun asList(): List<MangaOrderOptions> = values().toList()
    }
}

enum class ChapterOrderOptions(val option: String) {
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    PUBLISH_AT("publishAt"),
    READABLE_AT("readableAt"),
    VOLUME("volume"),
    CHAPTER("chapter");

    companion object {
        fun asList(): List<ChapterOrderOptions> = values().toList()
    }
}

object SortingOrder {
    const val ASC = "asc"
    const val DESC = "desc"
}