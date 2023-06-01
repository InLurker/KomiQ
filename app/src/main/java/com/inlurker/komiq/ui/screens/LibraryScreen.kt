package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
import com.inlurker.komiq.ui.screens.components.RoundedDropdownBox


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

    LargeTopAppBarComponent(
        title = "Library",
        onHistoryClick = {},
        onMoreClick = {}
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier =  Modifier
                .padding(horizontal = 16.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { },
                    onSearch = { searchActive = false },
                    active = searchActive,
                    onActiveChange = { },
                    placeholder = { Text(searchHint) },
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
            }
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
