package com.inlurker.komiq.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    var searchHintTitle by remember { mutableStateOf("Comic title") }
    var searchHintAuthor by remember { mutableStateOf("Comic Author") }
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
