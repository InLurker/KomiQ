package com.inlurker.komiq.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarComponent(
    title: String,
    onHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScrollToMinimzeLargeTopAppBar(
            title = title,
            onHistoryClick = onHistoryClick,
            onMoreClick = onMoreClick,
            scrollBehavior = scrollBehavior
        )
        content()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollToMinimzeLargeTopAppBar(
    title: String,
    onHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) = LargeTopAppBar(
    title = {
        Text(
            text = title
        )
    },
    actions = {
        IconButton(onClick = onHistoryClick) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "History"
            )
        }
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More"
            )
        }
    },
    scrollBehavior = scrollBehavior
)

@Preview
@Composable
fun TopAppBarComponentPreview() {
    LargeTopAppBarComponent(
        title = "Library",
        onHistoryClick = {},
        onMoreClick = {}
    ) {
    }
}