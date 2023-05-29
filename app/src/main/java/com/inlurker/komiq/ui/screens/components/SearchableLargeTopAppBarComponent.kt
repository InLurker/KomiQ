package com.inlurker.komiq.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableLargeTopAppBarComponent(
    title: String,
    onHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    searchHint: String,
    content: @Composable () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf(searchHint) }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background)
    ) {
        SearchableLargeTopAppBarComponent(
            title = title,
            onHistoryClick = onHistoryClick,
            onMoreClick = onMoreClick,
            scrollBehavior = scrollBehavior
        )
        Column(Modifier.padding(horizontal = 16.dp)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = onQueryChange,
                onSearch = { searchActive = false },
                active = searchActive,
                onActiveChange = onActiveChange,
                placeholder = { Text(hint) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                modifier = Modifier
            )
            {}
            content()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableLargeTopAppBarComponent(
    title: String,
    onHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) = LargeTopAppBar(
    title = {
        Text(
            text = title
        )
    },
    actions = {
        IconButton(onClick = onHistoryClick) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "History"
            )
        }
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More"
            )
        }
    },
    scrollBehavior = scrollBehavior
)

@Preview
@Composable
fun TopAppBarComponentPreview() {
    SearchableLargeTopAppBarComponent(
        title = "Library",
        onHistoryClick = {},
        onMoreClick = {},
        onQueryChange = { /* Handle search query change */ },
        onActiveChange = { /* Handle search bar activation change */ },
        searchHint = "Comic name",
    ) {
    }
}