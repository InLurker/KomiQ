package com.inlurker.komiq.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.palette.graphics.Palette
import com.inlurker.komiq.R
import com.inlurker.komiq.model.data.Comic
import com.inlurker.komiq.model.mangadexapi.builder.ComicSearchQuery
import com.inlurker.komiq.model.mangadexapi.getComicList
import com.inlurker.komiq.ui.screens.components.AddToLibraryButton
import com.inlurker.komiq.ui.screens.components.ChapterListElement
import com.inlurker.komiq.ui.screens.components.CollapsibleDescriptionComponent
import com.inlurker.komiq.ui.screens.helper.LoadImageFromUrl
import com.inlurker.komiq.ui.screens.helper.adjustLuminance
import com.inlurker.komiq.ui.screens.helper.generateColorPalette
import com.inlurker.komiq.viewmodel.ComicDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicDetailScreen(comic: Comic) {
    val context = LocalContext.current

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    val viewModel = ComicDetailViewModel(comic)

    val chapterPreview = viewModel.chapterPreview

    val genreList = viewModel.genreList

    var coverBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var paletteState by remember { mutableStateOf<Palette?>(null) }

    val primaryMaterialColor = MaterialTheme.colorScheme.primary
    val secondaryMaterialContainerColor = MaterialTheme.colorScheme.secondaryContainer
    val onPrimaryMaterialContainerColor = MaterialTheme.colorScheme.onPrimaryContainer


    var vibrantColor by remember { mutableStateOf(primaryMaterialColor) }
    var secondaryVibrantColor by remember { mutableStateOf(secondaryMaterialContainerColor) }
    var topAppBarIconButtonColor by remember { mutableStateOf(onPrimaryMaterialContainerColor) }

    val placeholderBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cover_placeholder)

    LoadImageFromUrl(viewModel.comicCoverUrl) { bitmap ->
        coverBitmap = bitmap
        val colorPalette = generateColorPalette(context, bitmap) // Use the temporary variable
        paletteState = colorPalette
    }

    if (coverBitmap != null) {
        val colorPalette = paletteState
        val vibrantSwatch = colorPalette?.vibrantSwatch

        vibrantColor = vibrantSwatch?.let { Color(it.rgb) } ?: primaryMaterialColor

        secondaryVibrantColor =
            if (vibrantColor != Color.Black && vibrantColor != Color.White) {
                adjustLuminance(vibrantColor, if (isSystemInDarkTheme()) 0.2f else 0.9f)
            } else {
                secondaryMaterialContainerColor
            }

        topAppBarIconButtonColor =
            if (vibrantColor != Color.Black && vibrantColor != Color.White) {
                adjustLuminance(vibrantColor, if (isSystemInDarkTheme()) 0.8f else 0.3f)
            } else {
                onPrimaryMaterialContainerColor
            }
    }

    Scaffold(
        topBar = {

        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValue ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(paddingValue)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentSize()
                ) {
                    Column(
                        modifier = Modifier
                            .zIndex(3f)
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    ) {
                        TopAppBar(
                            title = {},
                            navigationIcon = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowBack,
                                        contentDescription = "History",
                                        tint = topAppBarIconButtonColor
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.MoreVert,
                                        contentDescription = "More",
                                        tint = topAppBarIconButtonColor
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp)
                                .height(180.dp)
                        ) {
                            Image(
                                bitmap = coverBitmap?.asImageBitmap() ?: placeholderBitmap.asImageBitmap(),
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
                                    text = "2017, Ongoing",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )

                                Text(
                                    text = "Kusuriya no Hitorigoto",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 3
                                )

                                Text(
                                    text = "The Apothecary Diaries",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2
                                )

                                Spacer(Modifier.weight(1f))

                                AddToLibraryButton(
                                    vibrantColor = vibrantColor,
                                    tonalFilledColor = secondaryVibrantColor,
                                    isInLibrary = viewModel.isComicInLibrary,
                                    onToggleAction = { inLibrary ->
                                        viewModel.toggleComicInLibrary(inLibrary)
                                    }
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Hyuuga Natsu, Nanao Ikki, Nekokurage",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background.copy(0.3f),
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                            .zIndex(2f)
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(vibrantColor)
                            .zIndex(0f)
                    ) {
                        Image(
                            bitmap = coverBitmap?.asImageBitmap() ?: placeholderBitmap.asImageBitmap(),
                            contentDescription = "Blurred Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .matchParentSize()
                                .blur(3.dp)
                                .alpha(0.5f)
                        )
                    }

                    /*
                    BlurredAsyncImage(
                        context = context,
                        imageUrl = imageUrl,
                        contentDescription = "Blurred Cover",
                        contentScale = ContentScale.Crop,
                        blurRadius = 10f,
                        modifier = Modifier
                            .matchParentSize()
                            .zIndex(0f)
                            .background(dominantColor)
                            .alpha(0.4f)
                    )
                     */
                }
            }
            item {
                CollapsibleDescriptionComponent(
                    description = "Maomao, a young woman trained in the art of herbal medicine, is forced to work as a lowly servant in the inner palace. Though she yearns for life outside its perfumed halls, she isn’t long for a life of drudgery! Using her wits to break a “curse” afflicting the imperial heirs, Maomao attracts the attentions of the handsome eunuch Jinshi and is promoted to attendant food taster. But Jinshi has other plans for the erstwhile apothecary, and soon Maomao is back to brewing potions and…solving mysteries?!",
                    genreList = genreList,
                    genreTagColor = secondaryVibrantColor,
                    collapseTextButtonColor = vibrantColor,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        /* TODO */


                        Toast.makeText(context, viewModel.comicCoverUrl,Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(vibrantColor),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    Text(
                        text = "Start Reading Chapter 1",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Button(
                        onClick = { /*TODO*/ },
                        contentPadding = PaddingValues(0.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        modifier = Modifier
                            .height(36.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "140 Chapters",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter Chapters",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface,
                        thickness = 1.dp
                    )
                    chapterPreview?.let { chapter ->
                        for (i in 1..23) {
                            ChapterListElement(
                                chapterPreview = chapterPreview,
                                backgroundColor = secondaryVibrantColor,
                                onClick = { /* TODO */ }
                            )
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}



data class MangaChapterPreview(
    val volumeNumber: Int,
    val chapterNumber: Int,
    val chapterName: String,
    val uploadDate: Date,
    val scanlationGroup: String
)

@Preview
@Composable
fun MangaDetailScreenPreview() {
    var comicList by remember { mutableStateOf(emptyList<Comic>()) }

    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val query = ComicSearchQuery.Builder()
                .searchQuery("Mato seihei no")
                .sortingMethod("followedCount")
                .sortingOrder("desc")
                .comicAmount(10)
                .build()

            comicList = getComicList(query)
        }
    }

    if (comicList.isNotEmpty()) {
        ComicDetailScreen(comicList.first())
    }
}
