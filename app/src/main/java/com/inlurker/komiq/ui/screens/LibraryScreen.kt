package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inlurker.komiq.R
import com.inlurker.komiq.model.ComicPreviewModel
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.SortingToolbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {

    var comicList = listOf(
        ComicPreviewModel(
            title = "Chainsaw Man",
            author = listOf("Fujimoto Tatsuki")
        ),
        ComicPreviewModel(
            title = "Kono Subarashii sekai ni shukufuku wo!",
            author = listOf("Akatsuki Natsume", "Watari Masahito")
        ),
        ComicPreviewModel(
            title = "Kusuriya no Hitorigoto",
            author = listOf("Hyuuga Natsu", "Nanao Ikki", "Nekokurage")
        ),
        ComicPreviewModel(
            title = "SPY x FAMILY",
            author = listOf("Endou Tatsuya")
        ),
        ComicPreviewModel(
            title = "Chainsaw Man",
            author = listOf("Fujimoto Tatsuki")
        ),
        ComicPreviewModel(
            title = "Kono Subarashii sekai ni shukufuku wo!",
            author = listOf("Akatsuki Natsume", "Watari Masahito")
        ),
        ComicPreviewModel(
            title = "Kusuriya no Hitorigoto",
            author = listOf("Hyuuga Natsu", "Nanao Ikki", "Nekokurage")
        ),
        ComicPreviewModel(
            title = "SPY x FAMILY",
            author = listOf("Endou Tatsuya")
        ),
        ComicPreviewModel(
            title = "Chainsaw Man",
            author = listOf("Fujimoto Tatsuki")
        ),
        ComicPreviewModel(
            title = "Kono Subarashii sekai ni shukufuku wo!",
            author = listOf("Akatsuki Natsume", "Watari Masahito")
        ),
        ComicPreviewModel(
            title = "Kusuriya no Hitorigoto",
            author = listOf("Hyuuga Natsu", "Nanao Ikki", "Nekokurage")
        ),
        ComicPreviewModel(
            title = "SPY x FAMILY",
            author = listOf("Endou Tatsuya")
        )
    )

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
            items(comicList) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painterResource(id = R.drawable.spy),
                        contentDescription = "cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.author.joinToString(separator = ", "),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
