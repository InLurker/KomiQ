package com.inlurker.komiq.ui.navigation.navigationscreenmodel

sealed class ComicNavigationScreenModel(val route: String) {
    object Detail : ComicNavigationScreenModel(
        route = "detail/{comicId}"
    )
    object Reader: ComicNavigationScreenModel(
        route = "reader/{chapterId}"
    )
}