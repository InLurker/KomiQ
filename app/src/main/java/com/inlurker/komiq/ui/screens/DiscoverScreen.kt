package com.inlurker.komiq.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.SortingToolbar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiscoverScreen() {


    val sortingMethods = listOf("Last Updated", "Date Added")

    var searchQuery by remember { mutableStateOf("") }
    var searchedAction by remember { mutableStateOf(false) }
    var searchHint by remember { mutableStateOf("Search comic") }


    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)


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
            modifier = Modifier
                .padding(paddingValue)
                .padding(horizontal = 16.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                DockedSearchBar(
                    query = searchQuery,
                    onQueryChange = { query -> searchQuery = query }, // Update the search query
                    onSearch = { searchedAction = true },
                    active = false,
                    onActiveChange = { }, // Update the search activation
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        IconButton(onClick = { searchedAction = true }) {
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

                    },
                    onAscendingClicked = {

                    },
                    onFilterClicked = {

                    }
                )
            }
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier.height(160.dp))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DiscoverPreview() {
    DiscoverScreen()

}
