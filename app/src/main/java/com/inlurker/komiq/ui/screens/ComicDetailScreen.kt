package com.inlurker.komiq.ui.screens

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.ui.navigation.popUpToTop
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.AddToLibraryButton
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.CollapsibleDescriptionComponent
import com.inlurker.komiq.ui.screens.components.ChapterListElement
import com.inlurker.komiq.ui.screens.helper.ColorHelper.adjustLuminance
import com.inlurker.komiq.ui.screens.helper.ColorHelper.generateColorPalette
import com.inlurker.komiq.ui.screens.helper.Formatters.pluralize
import com.inlurker.komiq.ui.screens.helper.Formatters.removeTrailingZero
import com.inlurker.komiq.ui.screens.helper.ImageHelper.loadImageFromUrl
import com.inlurker.komiq.viewmodel.ComicDetailViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicDetailScreen(
    navController: NavController = rememberNavController(),
    viewModel: ComicDetailViewModel
) {
    val context = LocalContext.current

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    var paletteState by remember { mutableStateOf<Palette?>(null) }

    val primaryMaterialColor = MaterialTheme.colorScheme.primary
    val secondaryMaterialContainerColor = MaterialTheme.colorScheme.secondaryContainer
    val onPrimaryMaterialContainerColor = MaterialTheme.colorScheme.onPrimaryContainer

    var vibrantColor by remember { mutableStateOf(primaryMaterialColor) }
    var secondaryVibrantColor by remember { mutableStateOf(secondaryMaterialContainerColor) }
    var topAppBarIconButtonColor by remember { mutableStateOf(onPrimaryMaterialContainerColor) }


    val coverArtImageRequest = loadImageFromUrl(context, viewModel.comic.id, viewModel.comic.cover)

    val isDarkTheme = isSystemInDarkTheme()

    val painter = rememberAsyncImagePainter(
        coverArtImageRequest,
        contentScale = ContentScale.Crop,
        onSuccess = { state ->
            // Perform actions with the loaded image's bitmap
            val bitmap = state.result.drawable.toBitmap()
            paletteState = generateColorPalette(bitmap)
            val vibrantSwatch = paletteState?.vibrantSwatch

            vibrantColor = vibrantSwatch?.let { Color(it.rgb) } ?: primaryMaterialColor

            secondaryVibrantColor =
                if (vibrantColor != Color.Black && vibrantColor != Color.White) {
                    adjustLuminance(vibrantColor, if (isDarkTheme) 0.2f else 0.9f)
                } else {
                    secondaryMaterialContainerColor
                }

            topAppBarIconButtonColor =
                if (vibrantColor != Color.Black && vibrantColor != Color.White) {
                    adjustLuminance(vibrantColor, if (isDarkTheme) 0.8f else 0.3f)
                } else {
                    onPrimaryMaterialContainerColor
                }
        }
    )


    var chapterList by remember { mutableStateOf<List<Chapter>>(emptyList()) }
    var totalChapters by remember { mutableStateOf(0) }
    LaunchedEffect(viewModel.chapterList) {
        chapterList = viewModel.chapterList
        totalChapters = chapterList.size
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background)
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
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
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
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search",
                                    tint = topAppBarIconButtonColor
                                )
                            }
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
                            painter,
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
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = "${viewModel.comic.year}, ${
                                    viewModel.comic.status.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    }
                                }",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )

                            Text(
                                text = viewModel.comic.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 3
                            )

                            Text(
                                text = viewModel.comic.altTitle,
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
                                onToggleAction = {
                                    viewModel.toggleComicInLibrary()
                                }
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = viewModel.comic.authors.joinToString(", "),
                                style = MaterialTheme.typography.labelSmall,
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
                        painter,
                        contentDescription = "Blurred Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .blur(3.dp)
                            .alpha(0.5f)
                    )
                }
            }
        }
        item {
            CollapsibleDescriptionComponent(
                description = viewModel.comic.description,
                tagList = viewModel.genreList,
                genreTagColor = secondaryVibrantColor,
                collapseTextButtonColor = vibrantColor,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
        if (chapterList.isNotEmpty()) {
            item {
                Button(
                    onClick = {
                        navController.navigate("reader/${chapterList.last().id}") {
                            popUpToTop(navController)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(vibrantColor),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    Text(
                        text = "Start Reading Chapter ${removeTrailingZero(chapterList.last().chapter)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
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
                            text = pluralize(totalChapters, "Chapter"),
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
                if (chapterList.isNotEmpty()) {
                    chapterList.forEach { chapter ->
                        ChapterListElement(
                            volumeNumber = chapter.volume ?: 0f,
                            chapterNumber = chapter.chapter,
                            chapterName = chapter.title,
                            uploadDate = chapter.publishAt,
                            scanlationGroup = chapter.scanlationGroup,
                            backgroundColor = secondaryVibrantColor,
                            onClick = {
                                navController.navigate("reader/${chapter.id}") {
                                    popUpToTop(navController)
                                }
                            }
                        )
                        Divider(
                            color = Color.Gray,
                            thickness = 1.dp
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        color = secondaryVibrantColor,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview
@Composable
fun ComicDetailScreenPreview() {
    ComicRepository.initialize(LocalContext.current)
    //gorilla: a3f91d0b-02f5-4a3d-a2d0-f0bde7152370
    //mato: e1e38166-20e4-4468-9370-187f985c550e
    //mount celeb: 36d27f1d-122a-4c7e-9001-a0d62c8fb579
    ComicDetailScreen(viewModel = ComicDetailViewModel("d7037b2a-874a-4360-8a7b-07f2899152fd"))
}
    /*
    ComicDetailScreen(
        comic = Comic(
            id = "03426dd2-63c5-493e-853e-485d7c7dc9c0",
            type = "manga",
            attributes = Attributes(
                title = "Kono Subarashii Sekai ni Nichijou wo !",
                altTitle = "Everyday Life in This Wonderful World !",
                description = """
                    Spinoff of the KonoSuba series, featuring the daily life of Kazuma and Co .
                """.trimIndent(),
                originalLanguage = "ja",
                publicationDemographic = "shounen",
                status = "completed",
                year = 2016,
                contentRating = "safe",
                addedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(), lastReadChaper = 0
            ),
            authors = listOf("Akatsuki Natsume", "Somemiya Suzume"),
            tags = listOf(
                Tag(name = "Reincarnation", group = "theme"),
                Tag(name = "Monsters", group = "theme"),
                Tag(name = "Action", group = "genre"),
                Tag(name = "Demons", group = "theme"),
                Tag(name = "Animals", group = "theme"),
                Tag(name = "Romance", group = "genre"),
                Tag(name = "Comedy", group = "genre"),
                Tag(name = "Adventure", group = "genre"),
                Tag(name = "Magic", group = "theme"),
                Tag(name = "Harem", group = "theme"),
                Tag(name = "Isekai", group = "genre"),
                Tag(name = "Fantasy", group = "genre"),
                Tag(name = "Monster Girls", group = "theme"),
                Tag(name = "Slice of Life", group = "genre"),
                Tag(name = "Supernatural", group = "theme")
            ),
            cover = "c9ff96db-c443-4d93-8c14-2c6540b9f249.jpg",
            isInLibrary = false
        )
    )
     */
