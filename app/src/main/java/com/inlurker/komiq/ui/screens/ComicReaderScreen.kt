package com.inlurker.komiq.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inlurker.komiq.ui.screens.components.PageReaders.HorizontalPageReader
import com.inlurker.komiq.ui.screens.components.PageReaders.VerticalPageReader
import com.inlurker.komiq.ui.screens.components.SegmentedButton
import com.inlurker.komiq.ui.screens.components.SegmentedButtonItem
import com.inlurker.komiq.ui.screens.components.SettingComponents.ReaderBackgroundDropdownSettings
import com.inlurker.komiq.ui.screens.components.SettingComponents.ReadingDirectionDropdownSettings
import com.inlurker.komiq.ui.screens.components.SettingComponents.ReadingPreferencesSetting
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReaderBackground
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection
import com.inlurker.komiq.ui.screens.helper.Formatters.formatChapterVolume
import com.inlurker.komiq.ui.theme.KomiQTheme
import com.inlurker.komiq.viewmodel.ComicReaderViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComicReaderScreen(
    navController: NavController = rememberNavController(),
    viewModel: ComicReaderViewModel
) {
    val context = LocalContext.current

    val chapterPages = viewModel.chapterPages
    var currentPage by remember { mutableIntStateOf(1) } // Start from page 1
    val totalPages = chapterPages?.data?.size ?: 1
    val pagerState = rememberPagerState { totalPages } // Adjust initial page index

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
            val result = ColorMatrix()

            val brightness = (brightnessScale / 100f) * 0.8f + 0.2f
            val warmness = eyeCare / 100f
            val saturation = saturationScale / 100f

            // Adjust brightness
            result.setToScale(
                brightness,
                brightness,
                brightness,
                1f
            )

            // Adjust eyeCare Filter
            val eyeCareMatrix =
                ColorMatrix().apply {
                    setToScale(
                        1f,
                        1f - (warmness / 3),
                        1f - warmness,
                        1f
                    )
                }

            val saturationMatrix =
                ColorMatrix().apply {
                    setToSaturation(saturation)
                }

            result *= eyeCareMatrix
            result *= saturationMatrix

            if(isGreyscaleSelected) {
                result *= ColorMatrix().apply {
                    setToSaturation(0f)
                }
            }
            if(isInvertSelected) {
                result *= ColorMatrix(
                    floatArrayOf(
                        -1f, 0f, 0f, 0f, 255f,
                        0f, -1f, 0f, 0f, 255f,
                        0f, 0f, -1f, 0f, 255f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
            }

            ColorFilter.colorMatrix(
                colorMatrix = result
            )
        }
    }

    var topAppBarVisible by remember { mutableStateOf(true) }
    var bottomAppBarVisible by remember { mutableStateOf(true) }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            currentPage = page
        }
    }
    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(page = currentPage)
    }

    val scaffoldState = rememberBottomSheetScaffoldState()

    var selectedReaderBackground by remember { mutableStateOf(ReaderBackground.Default) }

    KomiQTheme(
        darkTheme = darkTheme
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 36.dp,
            sheetSwipeEnabled = true,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 32.dp)
                ) {
                    ReaderBackgroundDropdownSettings(
                        label = "Background Colour",
                        options = ReaderBackground.getOptionList(),
                        currentSelection = selectedReaderBackground,
                        onReaderBackgroundSelected = { selectedBackground ->
                            selectedReaderBackground = selectedBackground
                            darkTheme = when (selectedBackground) {
                                ReaderBackground.Light -> false
                                ReaderBackground.Dark -> true
                                else -> isSystemDarkMode
                            }
                        },
                        modifier = Modifier
                            .height(48.dp),
                        dropdownModifier = Modifier
                            .padding(horizontal = 32.dp)
                    )

                    ReadingDirectionDropdownSettings(
                        label = "Reading Layout",
                        options = ReadingDirection.getOptionList(),
                        currentSelection = selectedReadingDirection,
                        onReadingDirectionSelected = { selectedDirection ->
                            selectedReadingDirection = selectedDirection
                        },
                        modifier = Modifier
                            .height(48.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    ReadingPreferencesSetting(
                        eyeCare = eyeCare,
                        onEyeCareChange = { eyeCare = it },
                        brightnessScale = brightnessScale,
                        onBrightnessScaleChange = { brightnessScale = it },
                        saturationScale = saturationScale,
                        onSaturationScaleChange = { saturationScale = it }
                    )

                    Spacer(Modifier.height(8.dp))

                    SegmentedButton {
                        SegmentedButtonItem(
                            isSelected = isGreyscaleSelected,
                            onClick = { select ->
                                isGreyscaleSelected = select
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Greyscale")
                        }
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxHeight()  //fill the max height
                                .width(1.dp)
                        )
                        SegmentedButtonItem(
                            isSelected = isInvertSelected,
                            onClick = { select ->
                                isInvertSelected = select
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Invert")
                        }
                    }
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
                    Spacer(Modifier.weight(1f))
                    Row {
                        Spacer(Modifier.weight(1f))
                        when (selectedReadingDirection) {
                            ReadingDirection.LeftToRight, ReadingDirection.RightToLeft ->
                                HorizontalPageReader(
                                    pagerState = pagerState,
                                    chapterPages = chapterPages,
                                    colorFilter = colorFilter,
                                    readingDirection = selectedReadingDirection,
                                    context = context
                                )

                            ReadingDirection.TopToBottom, ReadingDirection.BottomToTop ->
                                VerticalPageReader(
                                    pagerState = pagerState,
                                    chapterPages = chapterPages,
                                    colorFilter = colorFilter,
                                    readingDirection = selectedReadingDirection,
                                    context = context
                                )
                        }
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
                                val chapterTitle = viewModel.chapter?.title?.let {
                                    it.ifBlank { "No Title" }
                                }
                                Text(
                                    text = chapterTitle ?: "Chapter Title",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = formatChapterVolume(
                                        volumeNumber = viewModel.chapter?.volume ?: 0f,
                                        chapterNumber = viewModel.chapter?.chapter ?: 0f
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

                AnimatedVisibility(
                    visible = bottomAppBarVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    modifier = Modifier
                        .zIndex(2f)
                        .align(Alignment.BottomCenter)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides sliderDirection) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(100))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = (currentPage + 1).toString(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(30.dp)
                            )
                            Slider(
                                value = currentPage.toFloat(),
                                onValueChange = { newValue ->
                                    currentPage = newValue.toInt()
                                },
                                valueRange = 0f..(totalPages - 1).toFloat(),
                                steps = if (totalPages > 2) totalPages - 2 else 0,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)
                                    .padding(vertical = 4.dp)
                            )
                            Text(
                                text = totalPages.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun ComicReaderScreenPreview() {
    ComicReaderScreen(viewModel = ComicReaderViewModel("a1bd9359-c160-4fb5-acfe-3f0423441841"))
}