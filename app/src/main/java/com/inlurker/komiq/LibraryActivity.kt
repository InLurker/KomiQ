
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class BottomNavItem(
    val name: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


val bottomNavItems = listOf(
    BottomNavItem(
        name = "Library",
        route = "library",
        selectedIcon = Icons.Filled.CollectionsBookmark,
        unselectedIcon = Icons.Outlined.CollectionsBookmark
    ),
    BottomNavItem(
        name = "Create",
        route = "add",
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {
    val navController = rememberNavController()

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var searchHintTitle by remember { mutableStateOf("Comic title") }
    var searchHintAuthor by remember { mutableStateOf("Comic Author") }
    val backStackEntry = navController.currentBackStackEntryAsState()



    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = "Library")
                },
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "History"
                        )
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "History"
                        )
                    }
                }
            )
        },
        bottomBar = {
            val navItems = listOf(
                BottomNavItem(
                    name = "Library",
                    route = "library",
                    selectedIcon = Icons.Filled.CollectionsBookmark,
                    unselectedIcon = Icons.Outlined.CollectionsBookmark
                ),
                BottomNavItem(
                    name = "Create",
                    route = "add",
                    selectedIcon = Icons.Filled.Explore,
                    unselectedIcon = Icons.Outlined.Explore
                )
            )

            var currentRoute by remember { mutableStateOf("library") }

            val selectedIndex = remember { mutableStateOf(0) }
            NavigationBar() {

                navItems.forEachIndexed { index, item ->
                    val isSelected = currentRoute == item.route
                    val icon = if (isSelected) item.selectedIcon else item.unselectedIcon
                    val fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = item.name
                            )
                        },
                        label = {
                            Text(
                                text = item.name,
                                fontWeight = fontWeight
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            selectedIndex.value = index
                        },
                        modifier = Modifier
                            .selectable(
                                selected = isSelected,
                                onClick = {
                                    selectedIndex.value = index
                                }
                            )
                    )
                }
            }

        }
    ) { paddingValue ->
        Column(Modifier.padding(paddingValue)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                },
                onSearch = { searchActive = false },
                active = searchActive,
                onActiveChange = {
                    searchActive = it
                },
                placeholder = {
                    Column {
                        Text(searchHintTitle)
                        Text(searchHintAuthor)
                    }
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LibraryPreview() {
    LibraryScreen()
}
