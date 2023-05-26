package com.inlurker.komiq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inlurker.komiq.navigation.model.BottomNavigationScreenModel
import com.inlurker.komiq.ui.screens.ExploreScreen
import com.inlurker.komiq.ui.screens.LibraryScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreenModel.Library.route
    ) {
        composable(route = BottomNavigationScreenModel.Library.route) {
            LibraryScreen()
        }
        composable(route = BottomNavigationScreenModel.Explore.route) {
            ExploreScreen()
        }
    }
}