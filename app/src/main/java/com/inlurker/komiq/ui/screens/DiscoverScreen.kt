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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inlurker.komiq.ui.navigation.popUpToTop
import com.inlurker.komiq.ui.screens.components.ComicCollectionComponent
import com.inlurker.komiq.ui.screens.components.ComicCollectionPlaceholder
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
    val sortingMethods = listOf("Last Updated", "Date Added")

    val searchQueryState = remember { mutableStateOf("") }
    val searchedActionState = remember { mutableStateOf(false) }
    val searchHintState = remember { mutableStateOf("Search comic") }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val lazyGridScrollState = rememberLazyGridState()

    LaunchedEffect(lazyGridScrollState.canScrollForward) {
        val totalItemsCount = viewModel.comicList.size
        val lastVisibleItemIndex = lazyGridScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        val nearEndOfList = lastVisibleItemIndex >= totalItemsCount - 1
        print(lastVisibleItemIndex)
        print(totalItemsCount)

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
                    query = searchQueryState.value,
                    onQueryChange = { query -> searchQueryState.value = query },
                    onSearch = { searchedActionState.value = true },
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text(searchHintState.value) },
                    leadingIcon = {
                        IconButton(onClick = { searchedActionState.value = true }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                ) {}
            }
            item(span = { GridItemSpan(3) }) {
                SortingToolbar(
                    sortingMethods = sortingMethods,
                    onSortingMethodSelected = { index, sortingMethod ->
                        // TODO: Implement sorting
                    },
                    onAscendingClicked = {
                        // TODO: Implement ascending sorting
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
