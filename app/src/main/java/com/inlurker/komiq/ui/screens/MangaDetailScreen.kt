package com.inlurker.komiq.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.text.format.DateFormat
import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.palette.graphics.Palette
import com.inlurker.komiq.R
import com.inlurker.komiq.ui.screens.components.CollapsibleDescriptionComponent
import com.inlurker.komiq.ui.screens.helper.adjustLuminance
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen() {
    val context = LocalContext.current

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val imageUrl =
        "https://mangadex.org/covers/e18fe8c6-f6dc-4f05-8462-7b2083ff9a6c/992fa6f5-c206-45e8-83cb-01e579c7adc7.jpg.256.jpg"

    var isMangaInLibrary by remember { mutableStateOf(false) }

    val drawableImageSource = R.drawable.apo
    val colorPalette = createPalette(context, drawableImageSource)
    val vibrantSwatch = colorPalette.vibrantSwatch

    val vibrantColor: Color =
        vibrantSwatch?.let { Color(it.rgb) } ?: MaterialTheme.colorScheme.primary

    val secondaryVibrantColor = if (vibrantColor != Color.Black && vibrantColor != Color.White) {
        adjustLuminance(vibrantColor, if (isSystemInDarkTheme()) 0.2f else 0.9f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val topAppBarIconButtonColor = if (vibrantColor != Color.Black && vibrantColor != Color.White) {
        adjustLuminance(vibrantColor, if (isSystemInDarkTheme()) 0.8f else 0.3f)
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    val GenreList = listOf(
        "Comedy",
        "Drama",
        "Historical",
        "Medical",
        "Mystery",
        "NewGenre",
        "NewGenre"
    )

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
                                    isInLibrary = isMangaInLibrary,
                                    onToggleAction = { inLibrary ->
                                        isMangaInLibrary = inLibrary
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
                            painter = painterResource(drawableImageSource),
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
                    genreList = GenreList,
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
                    val chapterPreview = MangaChapterPreview(
                        volumeNumber = 1,
                        chapterNumber = 23,
                        chapterName = "Chapter Name",
                        uploadDate = Date(),
                        scanlationGroup = "Scanlation Group"
                    )
                    for (i in 1..23) {
                        ChapterListElement(
                            chapterPreview = chapterPreview,
                            backgroundColor = secondaryVibrantColor
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

private fun createPalette(context: Context, drawable: Int): Palette {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawable)
    return Palette.from(bitmap).generate()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLibraryButton(
    vibrantColor: Color,
    tonalFilledColor: Color,
    isInLibrary: Boolean,
    onToggleAction: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = remember { Animatable(Color.Transparent) }

    var buttonText by remember { mutableStateOf("Add To Library") }

    LaunchedEffect(isInLibrary) {
        if (isInLibrary) {
            buttonText = "In Library"
            backgroundColor.animateTo(tonalFilledColor)
        } else {
            buttonText = "Add to Library"
            backgroundColor.animateTo(Color.Transparent)
        }
    }

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Button(
            onClick = { onToggleAction(!isInLibrary) },
            colors = ButtonDefaults.buttonColors(backgroundColor.value),
            contentPadding = PaddingValues(
                start = 6.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = 8.dp
            ),
            border = if (isInLibrary) null else BorderStroke(width = 0.3.dp, color = Color.Gray),
            modifier = modifier
                .defaultMinSize(
                    minHeight = 1.dp,
                    minWidth = 1.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Crossfade(targetState = isInLibrary) { isInLibrary ->
                    if (isInLibrary) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Filled Bookmark Symbol",
                            tint = vibrantColor,
                            modifier = Modifier.size(12.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "Outlined Bookmark Symbol",
                            tint = vibrantColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = buttonText,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 9.sp,
                    lineHeight = 10.sp,
                    letterSpacing = 0.1.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .animateContentSize()
                )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListElement(
    chapterPreview: MangaChapterPreview,
    backgroundColor: Color
) {
    val formattedUploadDate = remember {
        DateFormat.format("dd MMM yyyy", chapterPreview.uploadDate)
    }

    val chapterDetails = buildAnnotatedString {
        if (chapterPreview.volumeNumber != 0) {
            append("Vol ${chapterPreview.volumeNumber}. ")
        }
        append("Chapter ${chapterPreview.chapterNumber} - ")
        append(chapterPreview.chapterName.takeIf { it.isNotEmpty() } ?: "Unknown")
    }

    val chapterSourceInfo = buildAnnotatedString {
        append(formattedUploadDate)
        append(" · ")
        append(chapterPreview.scanlationGroup.takeIf { it.isNotEmpty() } ?: "Unknown")
    }

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Button(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor),
            modifier = Modifier
                .wrapContentSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = chapterDetails,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = chapterSourceInfo,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 10.sp,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
@Preview
@Composable
fun MangaDetailScreenPreview() {
    MangaDetailScreen()
}