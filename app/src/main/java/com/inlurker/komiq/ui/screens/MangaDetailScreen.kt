package com.inlurker.komiq.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
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
    val tagTextColor: Color = vibrantSwatch?.let { Color(it.bodyTextColor) } ?: MaterialTheme.colorScheme.onSecondaryContainer

    val luminanceModifier = if (isSystemInDarkTheme()) 0.8f else 0.2f
    val secondaryVibrantColor: Color = adjustLuminance(vibrantColor, luminanceModifier)
    val tertiaryVibrantColor: Color = adjustLuminance(vibrantColor, 1f - luminanceModifier)

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
                                        tint = secondaryVibrantColor
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Search,
                                        contentDescription = "History",
                                        tint = secondaryVibrantColor
                                    )
                                }
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.MoreVert,
                                        contentDescription = "More",
                                        tint = secondaryVibrantColor
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                                .height(150.dp)
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
                                    textAlign = TextAlign.Start
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
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White
                                    )
                                )
                            )
                            .zIndex(2f)
                    )
                    Image(
                        painter = painterResource(drawableImageSource),
                        contentDescription = "Blurred Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .blur(3.dp)
                            .zIndex(0f)
                            .background(vibrantColor)
                            .alpha(0.4f)
                    )
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
                Text(
                    text = "Maomao, a young woman trained in the art of herbal medicine, is forced to work as a lowly servant in the inner palace. Though she yearns for life outside its perfumed halls, she isn’t long for a life of drudgery! Using her wits to break a “curse” afflicting the imperial heirs, Maomao attracts the attentions of the handsome eunuch Jinshi and is promoted to attendant food taster. But Jinshi has other plans for the erstwhile apothecary, and soon Maomao is back to brewing potions and…solving mysteries?!",
                    fontSize = 8.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    lineHeight = 10.sp,
                    letterSpacing = 0.2.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .clipToBounds()
                ) {
                    for (genre in GenreList) {
                        PopularGenreTag(
                            text = genre,
                            backgroundColor = vibrantColor,
                            textColor = tagTextColor
                        )
                    }
                }
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
                    Box(
                        Modifier
                            .background(tertiaryVibrantColor)
                    ) {
                        Text(tertiaryVibrantColor.toString())
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
        fontSize = 8.sp,
        textAlign = TextAlign.Center,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        modifier = Modifier
            .background(
                backgroundColor, CircleShape
            )
            .padding(horizontal = 6.dp, vertical = 1.dp)
    )
}

@Preview
@Composable
fun MangaDetailScreenPreview() {
    MangaDetailScreen()
}