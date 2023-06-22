package com.inlurker.komiq.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inlurker.komiq.ui.navigation.NavigationGraph
import com.inlurker.komiq.ui.navigation.navigationscreenmodel.BottomNavigationScreenModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController = navController) },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        NavigationGraph(
            navController = navController
        )
    }
}

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomNavigationScreenModel.Library,
        BottomNavigationScreenModel.Discover
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (currentDestination?.route in listOf(
            BottomNavigationScreenModel.Library.route,
            BottomNavigationScreenModel.Discover.route
        )
    ) {
        NavigationBar {
            screens.forEach { screen ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                val contentColor = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
                NavigationBarItem(
                    icon = {
                        Crossfade(targetState = selected) { isSelected ->
                            if (isSelected)
                                Icon(
                                    imageVector = screen.selectedIcon,
                                    contentDescription = screen.title,
                                    tint = contentColor
                                )
                            else
                                Icon(
                                    imageVector = screen.unselectedIcon,
                                    contentDescription = screen.title,
                                    tint = contentColor
                                )
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                        )
                    },
                    selected = selected,
                    onClick = {
                        navHostController.navigate(screen.route) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}