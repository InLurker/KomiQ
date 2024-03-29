package com.inlurker.komiq.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inlurker.komiq.model.data.datamodel.Chapter
import com.inlurker.komiq.model.data.datamodel.Comic
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.data.repository.ComicRepository
import com.inlurker.komiq.ui.screens.components.PageImage
import com.inlurker.komiq.ui.screens.components.PageReaders.DynamicPageReader
import com.inlurker.komiq.ui.screens.components.ReaderSlider
import com.inlurker.komiq.ui.screens.components.SettingComponents.ReaderSettings
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReaderBackground
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection
import com.inlurker.komiq.ui.screens.helper.Formatters.formatChapterVolume
import com.inlurker.komiq.ui.screens.helper.ReaderHelper.calculateColorFilterMatrix
import com.inlurker.komiq.ui.theme.KomiQTheme
import com.inlurker.komiq.viewmodel.ComicReaderViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComicReaderScreen(
    navController: NavController = rememberNavController(),
    viewModel: ComicReaderViewModel = viewModel()
) {
    val pagerState = rememberPagerState { viewModel.pageUrls.size } // Adjust initial page index

    var selectedReadingDirection by remember { mutableStateOf(ReadingDirection.RightToLeft) }
    val sliderDirection by remember(selectedReadingDirection) {
        mutableStateOf(
            when (selectedReadingDirection) {
                ReadingDirection.LeftToRight, ReadingDirection.TopToBottom ->
                    LayoutDirection.Ltr
                else ->
                    LayoutDirection.Rtl
            }
        )
    }

    val isSystemDarkMode = isSystemInDarkTheme()
    var darkTheme by remember { mutableStateOf(isSystemDarkMode) }

    var eyeCare by remember { mutableIntStateOf(0) }
    var brightnessScale by remember { mutableIntStateOf(100) }
    var saturationScale by remember { mutableIntStateOf(100) }
    var isGreyscaleSelected by remember { mutableStateOf(false) }
    var isInvertSelected by remember { mutableStateOf(false) }

    val colorFilter by remember(eyeCare, brightnessScale, saturationScale, isGreyscaleSelected, isInvertSelected) {
        derivedStateOf {
            calculateColorFilterMatrix(eyeCare, brightnessScale, saturationScale, isGreyscaleSelected, isInvertSelected)
        }
    }

    var topAppBarVisible by remember { mutableStateOf(true) }
    var bottomAppBarVisible by remember { mutableStateOf(true) }

    val scaffoldState = rememberBottomSheetScaffoldState()

    var selectedReaderBackground by remember { mutableStateOf(ReaderBackground.Default) }

    KomiQTheme(
        darkTheme = darkTheme
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 48.dp,
            sheetSwipeEnabled = true,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 32.dp)
                ) {
                    ReaderSettings(
                        readerBackground = selectedReaderBackground,
                        onReaderBackgroundSelected = { selectedBackground ->
                            selectedReaderBackground = selectedBackground
                            darkTheme = when (selectedBackground) {
                                ReaderBackground.Light -> false
                                ReaderBackground.Dark -> true
                                else -> isSystemDarkMode
                            }
                        },
                        readingDirection = selectedReadingDirection,
                        onReadingDirectionSelected = { selectedReadingDirection = it },
                        eyeCareValue = eyeCare,
                        onEyeCareValueChange = { eyeCare = it },
                        brightnessValue = brightnessScale,
                        onBrightnessValueChange = { brightnessScale = it },
                        saturationValue = saturationScale,
                        onSaturationValueChange = { saturationScale = it },
                        isGreyscaleEnabled = isGreyscaleSelected,
                        onGreyscaleChanged = { isGreyscaleSelected = it },
                        isInvertEnabled = isInvertSelected,
                        onInvertChanged = { isInvertSelected = it }
                    )
                }
            }
        ) { paddingValue ->
            Box(
                modifier = Modifier
                    .padding(paddingValue)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // Disable ripple effect
                    ) {
                        topAppBarVisible = !topAppBarVisible
                        bottomAppBarVisible = !bottomAppBarVisible
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    var translate by remember {
                        mutableStateOf(false)
                    }
                    Spacer(Modifier.weight(1f))

                    val context = LocalContext.current

                    Button(onClick = {
                        if (!viewModel.isCraftInitialized()) {
                            viewModel.initializeCraft(context)
                        }
                        translate = !translate
                    }) {
                        Text("Toggle Translation: $translate")
                    }
                    Row {
                        Spacer(Modifier.weight(1f))
                        DynamicPageReader(
                            readingDirection = selectedReadingDirection,
                            pagerState = pagerState,
                            content = { page ->
                                val comicPage = viewModel.getComicPage(page, context).observeAsState()

                                if (translate && pagerState.currentPage == page) {
                                    val translatedPage = viewModel.getTranslatedPage(page, context).observeAsState()
                                    PageImage(
                                        data = translatedPage,
                                        colorFilter = colorFilter,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    
                                    if (translatedPage.value == null) {
                                        Text(text = "Translating...")
                                    }
                                } else {
                                    PageImage(
                                        data = comicPage,
                                        colorFilter = colorFilter,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .zIndex(0f)
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.weight(1f))
                    }

                    Spacer(Modifier.weight(1f))
                }

                AnimatedVisibility(
                    visible = topAppBarVisible,
                    enter = slideInVertically(initialOffsetY = { -it }),
                    exit = slideOutVertically(targetOffsetY = { -it }),
                    modifier = Modifier
                        .zIndex(2f)
                        .align(Alignment.TopCenter)
                ) {
                    TopAppBar(
                        title = {
                            Column {
                                val chapterTitle = viewModel.chapter.title.let {
                                    it.ifBlank { "No Title" }
                                }
                                Text(
                                    text = chapterTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = formatChapterVolume(
                                        volumeNumber = viewModel.chapter.volume ?: 0f,
                                        chapterNumber = viewModel.chapter.chapter
                                    ),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = "History"
                                )
                            }
                        }
                    )
                }

                ReaderSlider(
                    bottomAppBarVisible = bottomAppBarVisible,
                    sliderDirection = sliderDirection,
                    pagerState = pagerState,
                    modifier = Modifier
                        .zIndex(2f)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Preview
@Composable
fun ComicReaderPreview() {
    ComicRepository.currentComic = Comic(
        id = "",
        authors = listOf(),
        tags = listOf(),
        cover = "",
        isInLibrary = false,
        title = "",
        altTitle = "",
        description = "",
        languageSetting = ComicLanguageSetting.English,
        originalLanguage = "",
        publicationDemographic = null,
        status = "",
        year = 0,
        contentRating = "",
        addedAt = null,
        updatedAt = null,
        url = null,
        publicUrl = null
    )
    ComicRepository.currentChapter = Chapter(
        id = "a1bd9359-c160-4fb5-acfe-3f0423441841",
        volume = null,
        chapter = 0.0f,
        title = "",
        publishAt = LocalDateTime.now(),
        pages = 0,
        scanlationGroup = "",
        url = null,
    )


    ComicReaderScreen()
}