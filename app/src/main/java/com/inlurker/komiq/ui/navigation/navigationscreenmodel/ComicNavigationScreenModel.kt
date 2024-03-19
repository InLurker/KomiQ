package com.inlurker.komiq.ui.navigation.navigationscreenmodel

sealed class ComicNavigationScreenModel(val route: String) {
    object Detail : ComicNavigationScreenModel(
        route = "detail"
    )
    object Reader: ComicNavigationScreenModel(
        route = "reader"
    )
}