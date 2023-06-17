package com.inlurker.komiq.ui.navigation.navigationscreenmodel

sealed class ComicNavigationScreenModel(val route: String) {
    object Detail : ComicNavigationScreenModel("detail/{mangaId}") {
        fun createRoute(mangaId: String): String = "detail/$mangaId"
    }
}
