package com.inlurker.komiq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inlurker.komiq.ui.navigation.navigationscreenmodel.BottomNavigationScreenModel
import com.inlurker.komiq.ui.navigation.navigationscreenmodel.ComicNavigationScreenModel
import com.inlurker.komiq.ui.screens.ComicDetailScreen
import com.inlurker.komiq.ui.screens.ComicReaderScreen
import com.inlurker.komiq.ui.screens.DiscoverScreen
import com.inlurker.komiq.ui.screens.LibraryScreen
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
        composable(route = ComicNavigationScreenModel.Detail.route
        ) {
            ComicDetailScreen(navController)
        }
        composable(route = ComicNavigationScreenModel.Reader.route) { backStackEntry ->
            ComicReaderScreen(navController)
        }
    }
}
