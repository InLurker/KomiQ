package com.inlurker.komiq.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.palette.graphics.Palette
import com.inlurker.komiq.R
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.SortingToolbar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiscoverScreen() {

    val context = LocalContext.current

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
        if (searchedAction) {
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
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
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
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(paddingValue)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    DockedSearchBar(
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
                        }
                    ) {}
                }
                item {
                    Column {
                        Text(
                            text = "Popular Now",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(
                            modifier = Modifier
                        ) {
                            item {
                                ElevatedCard(
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .width(310.dp)
                                        .height(160.dp)
                                        .padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.background)
                                            .fillMaxSize()
                                    ) {
                                        val drawableImageSource = R.drawable.apo
                                        val dominantPalette =
                                            createPalette(context, drawableImageSource).dominantSwatch!!
                                        val tagColor = Color(dominantPalette.rgb)
                                        val textColor = Color(dominantPalette.bodyTextColor)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp)
                                                .zIndex(1f)
                                        ) {
                                            Image(
                                                painterResource(id = drawableImageSource),
                                                contentDescription = "cover",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .aspectRatio(2f / 3f)
                                                    .clip(RoundedCornerShape(10.dp))
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .padding(horizontal = 10.dp)
                                                    .padding(top = 10.dp)
                                            ) {
                                                Text(
                                                    text = "Hyuuga Natsu",
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = "Kusuriya no Hitorigoto",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    textAlign = TextAlign.Start,

                                                )
                                                Spacer(
                                                    Modifier.height(4.dp)
                                                )
                                                val GenreList = listOf(
                                                    "Comedy",
                                                    "Drama",
                                                    "Historical",
                                                    "Medical",
                                                    "Mystery"
                                                )
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                                    modifier = Modifier
                                                        .wrapContentWidth(Alignment.Start)
                                                ) {
                                                    for (genre in GenreList) {
                                                        PopularGenreTag(
                                                            text = genre,
                                                            backgroundColor = tagColor,
                                                            textColor = textColor
                                                        )
                                                    }
                                                }
                                                Spacer(
                                                    Modifier.height(4.dp)
                                                )
                                                Text(
                                                    text = "Maomao, a young woman trained in the art of herbal medicine, is forced to work as a lowly servant in the inner palace. Though she yearns for life outside its perfumed halls, she isn’t long for a life of drudgery! Using her wits to break a “curse” afflicting the imperial heirs, Maomao attracts the attentions of the handsome eunuch Jinshi and is promoted to attendant food taster. But Jinshi has other plans for the erstwhile apothecary, and soon Maomao is back to brewing potions and…solving mysteries?!",
                                                    fontSize = 7.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    textAlign = TextAlign.Start
                                                )
                                            }
                                        }
                                        Image(
                                            painter = painterResource(id = drawableImageSource),
                                            contentDescription = "cover",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .blur(10.dp)
                                                .alpha(0.4f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createPalette(context: Context, drawable: Int): Palette {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawable)
    return Palette.from(bitmap).generate()
}

@Composable
fun PopularGenreTag(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Text(
        text = text,
        fontSize = 7.sp,
        textAlign = TextAlign.Center,
        color = textColor,
        maxLines = 1,
        modifier = Modifier
            .background(
                backgroundColor,
                CircleShape
            )
            .padding(horizontal = 6.dp, vertical = 1.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun DiscoverPreview() {
    DiscoverScreen()
}
