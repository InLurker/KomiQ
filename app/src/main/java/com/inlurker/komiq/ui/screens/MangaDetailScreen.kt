package com.inlurker.komiq.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen() {
    val context = LocalContext.current

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val imageUrl = "https://mangadex.org/covers/e18fe8c6-f6dc-4f05-8462-7b2083ff9a6c/992fa6f5-c206-45e8-83cb-01e579c7adc7.jpg.256.jpg"

    val drawableImageSource = R.drawable.apo
    val colorPalette = createPalette(context, drawableImageSource)
    val vibrantSwatch = colorPalette.vibrantSwatch

    val vibrantColor: Color = vibrantSwatch?.let { Color(it.rgb) } ?: MaterialTheme.colorScheme.secondaryContainer

    val secondaryVibrantColor = if (vibrantColor != Color.Black && vibrantColor != Color.White) {
        adjustLuminance(vibrantColor, if (isSystemInDarkTheme()) 0.3f else 0.8f)
    } else {
        vibrantColor
    }

    val topAppBarIconButtonColor = if (vibrantColor != Color.Black && vibrantColor != Color.White) {
        adjustLuminance(vibrantColor, if (isSystemInDarkTheme()) 0.8f else 0.3f)
    } else {
        vibrantColor
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
                    ){
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
                                        contentDescription = "History",
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
                                    style = MaterialTheme.typography.labelMedium,
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
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2
                                )
                                Spacer(Modifier.weight(1f))
                                FilledTonalButton(
                                    onClick = { /*TODO*/ },
                                    contentPadding = PaddingValues(0.dp),

                                    modifier = Modifier
                                        .height(12.dp)
                                        .padding(0.dp)
                                ) {
                                    Row {
                                        Text(
                                            text = "In Library",
                                            fontSize = 5.sp
                                        )
                                    }
                                }
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
                    Box(modifier = Modifier
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
                        .padding(vertical = 8.dp)
                )
            }
            item {
                Column {
                    Box(
                        Modifier
                            .background(vibrantColor)
                    ) {
                        Text(vibrantColor.toString())
                    }
                    Box(
                        Modifier
                            .background(secondaryVibrantColor)
                    ) {
                        Text(secondaryVibrantColor.toString())
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


@Preview
@Composable
fun MangaDetailScreenPreview() {
    MangaDetailScreen()
}