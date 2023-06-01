package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.RoundedDropdownBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen() {
    val sortingMethods = listOf("Last Updated", "Date Added")

    var searchQuery by remember { mutableStateOf("") }
    var searchedAction by remember { mutableStateOf(false) }
    var searchHint by remember { mutableStateOf("Search comic") }

    LargeTopAppBarComponent(
        title = "Explore",
        onHistoryClick = {},
        onMoreClick = {}
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { query -> searchQuery = query }, // Update the search query
                    onSearch = { searchedAction = true },
                    active = false,
                    onActiveChange = { }, // Update the search activation
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                ) {}
            }
            if (searchedAction) {
                item(span = { GridItemSpan(3) }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedDropdownBox(
                            items = sortingMethods,
                            selectedItem = sortingMethods[0],
                            onItemSelected = { index, sortMethod ->

                            }
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Ascending",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Ascending",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Outlined.GridView,
                            contentDescription = "Ascending",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {

            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun ExplorePreview() {
    ExploreScreen()
}
