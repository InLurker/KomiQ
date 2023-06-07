package com.inlurker.komiq.ui.screens

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
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
import com.inlurker.komiq.ui.screens.helper.generateMonochromaticPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen() {
    val context = LocalContext.current

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val drawableImageSource = R.drawable.apo
    val colorPalette = createPalette(context, drawableImageSource)
    val dominantSwatch = colorPalette.dominantSwatch

    val tagColor: Color
    val tagTextColor: Color

    val monochromaticPalette: List<Color>

    if (dominantSwatch != null) {
        tagColor = Color(dominantSwatch.rgb)
        tagTextColor = Color(dominantSwatch.bodyTextColor)
        monochromaticPalette = generateMonochromaticPalette(dominantSwatch.rgb, 5)

    } else {
        tagColor = MaterialTheme.colorScheme.secondaryContainer
        tagTextColor = MaterialTheme.colorScheme.onSecondaryContainer
        monochromaticPalette = generateMonochromaticPalette(MaterialTheme.colorScheme.primary.hashCode(), 5)
    }



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
                            .zIndex(1f)
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    ){
                        TopAppBar(
                            title = {},
                            navigationIcon = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "History"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Search,
                                        contentDescription = "History"
                                    )
                                }
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = Icons.Outlined.MoreVert,
                                        contentDescription = "More"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Transparent)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .height(150.dp)
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
                                    textAlign = TextAlign.Start
                                )
                                Spacer(
                                    Modifier.height(4.dp)
                                )
                                val GenreList = listOf(
                                    "Comedy",
                                    "Drama",
                                    "Historical",
                                    "Medical",
                                    "Mystery",
                                    "NewGenre",
                                    "NewGenre"
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
                                            backgroundColor = tagColor,
                                            textColor = tagTextColor
                                        )
                                    }
                                }
                                Spacer(
                                    Modifier.height(4.dp)
                                )
                                Text(
                                    text = "Maomao, a young woman trained in the art of herbal medicine, is forced to work as a lowly servant in the inner palace. Though she yearns for life outside its perfumed halls, she isn’t long for a life of drudgery! Using her wits to break a “curse” afflicting the imperial heirs, Maomao attracts the attentions of the handsome eunuch Jinshi and is promoted to attendant food taster. But Jinshi has other plans for the erstwhile apothecary, and soon Maomao is back to brewing potions and…solving mysteries?!",
                                    fontSize = 8.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Start,
                                    lineHeight = 10.sp,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
                    }
                    Image(
                        painter = painterResource(id = drawableImageSource),
                        contentDescription = "cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .blur(10.dp)
                            .alpha(0.4f)
                            .zIndex(0f)
                    )
                }
            }
            item {
                Column {
                    monochromaticPalette.forEach { color ->
                        Box(
                            Modifier
                                .background(color)
                        ) {
                            Text(color.toString())
                        }
                    }
                }
                Box(
                    Modifier
                        .background(tagColor)
                ) {
                    Text("original")
                    Text(tagColor.toString())
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