package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.LaunchedEffect
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
import com.inlurker.komiq.model.data.mangadexapi.constants.MangaOrderOptions
import com.inlurker.komiq.model.data.mangadexapi.constants.SortingOrder
import com.inlurker.komiq.ui.navigation.popUpToTop
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.ComicCollectionPlaceholder
import com.inlurker.komiq.ui.screens.components.ComicCollectionComponent
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.SortingToolbar
import com.inlurker.komiq.viewmodel.DiscoverViewModel
import com.inlurker.komiq.viewmodel.paging.ListState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navController: NavController = rememberNavController(),
    viewModel: DiscoverViewModel = viewModel()
) {
    val sortingMethods = listOf(
        MangaOrderOptions.FOLLOWED_COUNT to "Popularity",
        MangaOrderOptions.RELEVANCE to "Relevance",
        MangaOrderOptions.LATEST_UPLOADED_CHAPTER to "Latest",
        MangaOrderOptions.UPDATED_AT to "Updated",
        MangaOrderOptions.CREATED_AT to "Created",
        MangaOrderOptions.TITLE to "Alphabetical",
        MangaOrderOptions.YEAR to "Year"
    )

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val searchHint = "Search comic"
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    var isDescending  by remember { mutableStateOf(true) }

    val lazyGridScrollState = rememberLazyGridState()

    val onSearchAction= {
        searchActive = true
        viewModel.searchQuery = searchQuery
        viewModel.updateSearchQuery()
        viewModel.resetComicList()
    }

    val onClearSearchAction = {
        searchActive = false
        searchQuery = ""
        viewModel.searchQuery = ""
        viewModel.updateSearchQuery()
        viewModel.resetComicList()
    }

    LaunchedEffect(lazyGridScrollState.canScrollForward) {
        val totalItemsCount = viewModel.comicList.size
        val lastVisibleItemIndex = lazyGridScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        val nearEndOfList = lastVisibleItemIndex >= totalItemsCount - 8

        if (nearEndOfList) {
            viewModel.loadNextPage()
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBarComponent(
                title = "Explore",
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
            state = lazyGridScrollState,
            modifier = Modifier
                .padding(paddingValue)
                .padding(horizontal = 16.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                DockedSearchBar(
                    query = searchQuery,
                    onQueryChange = { query ->
                        searchQuery = query
                        if (query == "") {
                            onClearSearchAction()
                        }
                    },
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            onSearchAction()
                        }
                    },
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        if (searchActive) {
                            IconButton(
                                onClick = { onClearSearchAction() }
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Cancel Search",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    if (searchQuery.isNotBlank()) {
                                        onSearchAction()
                                    }
                                }
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
            item(span = { GridItemSpan(3) }) {
                SortingToolbar(
                    sortingMethods = sortingMethods.map { it.second },
                    onSortingMethodSelected = { index, sortingMethod ->
                        viewModel.sortingMethod = sortingMethods[index].first
                    },
                    isSortingOrderDescending = isDescending,
                    onSortingOrderClicked = { toggleResult ->
                        isDescending = toggleResult
                        viewModel.sortingOrder = if (isDescending) SortingOrder.DESC else SortingOrder.ASC
                    },
                    onFilterClicked = {
                        // TODO: Implement filtering
                    }
                )
            }

            // Comic list
            itemsIndexed(viewModel.comicList) { index, comic ->
                ComicCollectionComponent(
                    comic = comic,
                    onClick = {
                        navController.navigate("detail/${comic.id}") {
                            popUpToTop(navController)
                        }
                    }
                )
            }
            // Pagination progress indicator
            when (viewModel.listState) {
                ListState.LOADING, ListState.IDLE -> {
                    item {
                        ComicCollectionPlaceholder()
                    }
                }
                ListState.ERROR -> {
                    item(span = { GridItemSpan(3) }) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ){
                            Text("An unknown error occurred.")
                        }
                    }
                }
                else -> Unit
            }
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier
                    .height(120.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DiscoverPreview() {
    DiscoverScreen()
}
