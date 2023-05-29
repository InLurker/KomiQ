package com.inlurker.komiq.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.inlurker.komiq.ui.screens.components.SearchableLargeTopAppBarComponent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen() {

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var searchHint by remember { mutableStateOf("Search comic") }
    SearchableLargeTopAppBarComponent(
        title = "Explore",
        onHistoryClick = {},
        onMoreClick = {},
        onQueryChange = { /* Handle search query change */ },
        onActiveChange = { /* Handle search bar activation change */ },
        searchHint = "Search"
    ) {

    }
}





@Preview(showBackground = true)
@Composable
fun ExplorePreview() {
    ExploreScreen()
}
