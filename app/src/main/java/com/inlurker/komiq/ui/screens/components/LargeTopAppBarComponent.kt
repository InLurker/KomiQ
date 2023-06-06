package com.inlurker.komiq.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inlurker.komiq.ui.screens.LibraryScreen

@ExperimentalMaterial3Api
@Composable
fun LargeTopAppBarComponent(
    title: String,
    onHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) =
    LargeTopAppBar(
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
    LibraryScreen()
}