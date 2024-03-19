package com.inlurker.komiq.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inlurker.komiq.model.data.kotatsu.KotatsuPagedMangaParser
import com.inlurker.komiq.model.data.kotatsu.util.InMemoryCookieJar
import com.inlurker.komiq.model.data.mangadexapi.constants.MangaOrderOptions
import com.inlurker.komiq.model.data.mangadexapi.constants.SortingOrder
import com.inlurker.komiq.ui.navigation.popUpToTop
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.ComicCollectionPlaceholder
import com.inlurker.komiq.ui.screens.components.ComicCollectionComponent
import com.inlurker.komiq.ui.screens.components.LargeTopAppBarComponent
import com.inlurker.komiq.ui.screens.components.SettingComponents.FilterSearchSetting
import com.inlurker.komiq.ui.screens.components.SortingToolbar
import com.inlurker.komiq.viewmodel.DiscoverViewModel
import com.inlurker.komiq.viewmodel.paging.ListState
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koitharu.kotatsu.parsers.InternalParsersApi
import org.koitharu.kotatsu.parsers.KotatsuMangaLoaderContext
import org.koitharu.kotatsu.parsers.model.MangaSource


@OptIn(ExperimentalMaterial3Api::class, InternalParsersApi::class)
@Composable
fun DiscoverScreen(
    navController: NavController = rememberNavController(),
    viewModel: DiscoverViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }
    val searchHint = "Search comic"
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    var isDescending  by remember { mutableStateOf(true) }

    val lazyGridScrollState = rememberLazyGridState()

    val kotatsuLoaderContext = KotatsuMangaLoaderContext(
        OkHttpClient(),
        InMemoryCookieJar(),
        LocalContext.current
    )

    viewModel.kotatsuParser = KotatsuPagedMangaParser(MangaSource.RAWKUMA, kotatsuLoaderContext)

    val refreshSearchAction = {
        viewModel.updateSearchQuery()
        viewModel.resetComicList()
    }

    val onSearchAction= {
        searchActive = true
        viewModel.searchQuery = searchQuery
        refreshSearchAction()
    }

    val onClearSearchAction = {
        searchActive = false
        searchQuery = ""
        viewModel.searchQuery = ""
        refreshSearchAction()
    }


    LaunchedEffect(lazyGridScrollState.canScrollForward) {
        val totalItemsCount = viewModel.comicList.size
        val lastVisibleItemIndex = lazyGridScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        val nearEndOfList = lastVisibleItemIndex >= totalItemsCount - 10

        if (nearEndOfList) {
            viewModel.loadNextPage()
        }
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            LargeTopAppBarComponent(
                title = "Explore",
                onHistoryDropdown = { /*TODO*/ },
                onMoreDropdown = { /*TODO*/ },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValue ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = lazyGridScrollState,
            modifier = Modifier
                .padding(paddingValue)
                .padding(horizontal = 16.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                DockedSearchBar(
                    query = searchQuery,
                    onQueryChange = { query ->
                        searchQuery = query
                        if (query == "") {
                            onClearSearchAction()
                        }
                    },
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            onSearchAction()
                        }
                    },
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text(searchHint) },
                    leadingIcon = {
                        if (searchActive) {
                            IconButton(
                                onClick = { onClearSearchAction() }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Outlined.ArrowBack,
                                    contentDescription = "Cancel Search",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    if (searchQuery.isNotBlank()) {
                                        onSearchAction()
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    trailingIcon = {
                        if (searchActive)
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear Search Query",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                    },
                ) {}
            }
            item(span = { GridItemSpan(3) }) {
                val sortingMethods = listOf(
                    MangaOrderOptions.FOLLOWED_COUNT to "Popularity",
                    MangaOrderOptions.RELEVANCE to "Relevance",
                    MangaOrderOptions.LATEST_UPLOADED_CHAPTER to "Latest",
                    MangaOrderOptions.UPDATED_AT to "Updated",
                    MangaOrderOptions.CREATED_AT to "Created",
                    MangaOrderOptions.TITLE to "Alphabetical",
                    MangaOrderOptions.YEAR to "Year"
                )
                SortingToolbar(
                    sortingMethods = sortingMethods.map { it.second },
                    onSortingMethodSelected = { index, _ ->
                        viewModel.sortingMethod = sortingMethods[index].first
                        viewModel.updateSearchQuery()
                        viewModel.resetComicList()
                    },
                    isSortingOrderDescending = isDescending,
                    onSortingOrderClicked = { toggleResult ->
                        isDescending = toggleResult
                        viewModel.sortingOrder = if (toggleResult) SortingOrder.DESC else SortingOrder.ASC
                        viewModel.updateSearchQuery()
                        viewModel.resetComicList()
                    },
                    onFilterClicked = {
                        showBottomSheet = true
                    }
                )
            }

            // Comic list
            itemsIndexed(viewModel.comicList) { _, comic ->
                ComicCollectionComponent(
                    comic = comic,
                    onClick = {
                        navController.navigate("detail/${comic.languageSetting.isoCode+ "_" + comic.id}") {
                            popUpToTop(navController)
                        }
                    }
                )
            }
            // Pagination progress indicator
            when (viewModel.listState) {
                ListState.LOADING, ListState.IDLE -> {
                    item {
                        ComicCollectionPlaceholder()
                    }
                }
                ListState.ERROR -> {
                    item(span = { GridItemSpan(3) }) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ){
                            Text("An unknown error occurred.")
                        }
                    }
                }
                else -> Unit
            }
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier
                    .height(120.dp)
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                FilterSearchSetting(
                    currentComicLanguageSetting = viewModel.comicLanguageSetting,
                    currentGenreFilter = viewModel.genreFilter,
                    currentThemeFilter = viewModel.themeFilter,
                    onApplySettings = { selectedLanguage, selectedGenre, selectedTheme ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        if (viewModel.comicLanguageSetting != selectedLanguage ||
                            viewModel.genreFilter != selectedGenre ||
                            viewModel.themeFilter != selectedTheme
                        ) {
                            viewModel.comicLanguageSetting = selectedLanguage
                            viewModel.genreFilter = selectedGenre
                            viewModel.themeFilter = selectedTheme

                            refreshSearchAction()
                        }
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DiscoverPreview() {
    DiscoverScreen()
}
