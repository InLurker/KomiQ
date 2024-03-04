package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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

    val sortingMethods = listOf(
        SortOption.LAST_ADDED to "Last Added",
        SortOption.TITLE to  "Title",
        SortOption.AUTHOR to "Author",
        SortOption.YEAR to "Year"
    )

    var selectedSortingMethod by remember { mutableStateOf(SortOption.LAST_ADDED) }
    var isDescending  by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val searchHint = "Search comic"

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)


    val filteredComics by remember(searchQuery, viewModel.comics, isDescending) {
        mutableStateOf(
            viewModel.comics.filter { comic ->
                val searchQueryLowercase = searchQuery.lowercase()
                comic.title.lowercase().contains(searchQueryLowercase) ||
                        comic.altTitle.lowercase().contains(searchQueryLowercase) ||
                        comic.authors.any { author -> author.lowercase().contains(searchQueryLowercase) }
            }
        )
    }

    val sortedComics = when (selectedSortingMethod) {
        SortOption.LAST_ADDED -> filteredComics // No sorting needed since it's the default order
        SortOption.TITLE -> filteredComics.sortedBy { comic -> comic.title }
        SortOption.AUTHOR -> filteredComics.sortedBy { comic -> comic.authors.firstOrNull() }
        SortOption.YEAR -> filteredComics.sortedBy { comic -> comic.year }
    }.let { if (isDescending) it.reversed() else it }

    Scaffold(
        topBar = {
            LargeTopAppBarComponent(
                title = "Library",
                onMoreDropdown = { /*TODO*/ },
                onHistoryDropdown = { /*TODO*/ },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                    onQueryChange = { query ->
                        searchQuery = query
                        searchActive = query != ""
                    },
                    onSearch = {},
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        if (searchActive) {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    searchActive = false
                                }
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Cancel Search",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        if (searchActive)
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear Search Query",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                    },
                ) {}
            }
            item(
                span = { GridItemSpan(3) }
            ) {
                SortingToolbar(
                    sortingMethods = sortingMethods.map { it.second },
                    onSortingMethodSelected = { index, sortingMethod ->
                        selectedSortingMethod = sortingMethods[index].first
                    },
                    isSortingOrderDescending = isDescending,
                    onSortingOrderClicked = { toggleResult ->
                        isDescending = toggleResult
                    },
                    onFilterClicked = {
                        // TODO: Implement filtering
                    }
                )
            }
            items(sortedComics) { comic ->
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

enum class SortOption {
    LAST_ADDED,
    TITLE,
    AUTHOR,
    YEAR
}

@Preview
@Composable
fun LibraryPreview() {
    LibraryScreen()
}
