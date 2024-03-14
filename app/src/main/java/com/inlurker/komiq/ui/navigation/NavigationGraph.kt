package com.inlurker.komiq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.ui.navigation.navigationscreenmodel.BottomNavigationScreenModel
import com.inlurker.komiq.ui.navigation.navigationscreenmodel.ComicNavigationScreenModel
import com.inlurker.komiq.ui.screens.ComicDetailScreen
import com.inlurker.komiq.ui.screens.ComicReaderScreen
import com.inlurker.komiq.ui.screens.DiscoverScreen
import com.inlurker.komiq.ui.screens.LibraryScreen
import com.inlurker.komiq.viewmodel.ComicDetailViewModel
import com.inlurker.komiq.viewmodel.ComicReaderViewModel

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreenModel.Library.route
    ) {
        composable(route = BottomNavigationScreenModel.Library.route) {
            LibraryScreen(navController)
        }
        composable(route = BottomNavigationScreenModel.Discover.route) {
            DiscoverScreen(navController)
        }
        composable(
            route = ComicNavigationScreenModel.Detail.route,
            arguments = listOf(
                navArgument("comicId") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.let { argument ->
                argument.getString("comicId")?.let { comicId ->
                    argument.getString("language")?.let { language ->
                        val languageSetting = ComicLanguageSetting.fromIsoCode(language)!!
                        ComicDetailScreen(navController, ComicDetailViewModel(comicId, languageSetting))
                    }
                }
            }
        }
        composable(
            route = ComicNavigationScreenModel.Reader.route,
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { argument ->
                argument.getString("chapterId")?.let { chapterId ->
                    ComicReaderScreen(navController, ComicReaderViewModel(chapterId))
                }
            }
        }
    }
}
