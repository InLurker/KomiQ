package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inlurker.komiq.ui.navigation.popUpToTop
import com.inlurker.komiq.ui.screens.components.ComicCollectionComponent
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.SortingToolbar
import com.inlurker.komiq.viewmodel.LibraryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navController: NavController = rememberNavController(),
    viewModel: LibraryViewModel = viewModel()
) {

    val sortingMethods = listOf("Last Updated", "Date Added")

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var searchHint by remember { mutableStateOf("") }

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            LargeTopAppBarComponent(
                title = "Library",
                onHistoryClick = { /*TODO*/ },
                onMoreClick = { /*TODO*/ },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValue ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(paddingValue)
                .padding(horizontal = 16.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                DockedSearchBar(
                    query = searchQuery,
                    onQueryChange = { },
                    onSearch = { searchActive = false },
                    active = searchActive,
                    onActiveChange = { },
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
                {}
            }
            item(
                span = { GridItemSpan(3) }
            ) {
                SortingToolbar(
                    sortingMethods = sortingMethods,
                    onSortingMethodSelected = { index, sortingMethod ->

                    },
                    onAscendingClicked = {

                    },
                    onFilterClicked = {

                    }
                )
            }
            items(viewModel.comics) { comic ->
                ComicCollectionComponent(
                    comic = comic,
                    onClick = {
                        navController.navigate("detail/${comic.id}") {
                            popUpToTop(navController)
                        }
                    }
                )
            }
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier.height(160.dp))
            }
        }
    }
}


@Preview
@Composable
fun LibraryPreview() {
    LibraryScreen()
}
