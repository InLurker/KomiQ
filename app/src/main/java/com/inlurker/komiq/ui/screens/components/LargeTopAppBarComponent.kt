package com.inlurker.komiq.ui.screens.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.inlurker.komiq.ui.screens.LibraryScreen

@ExperimentalMaterial3Api
@Composable
fun LargeTopAppBarComponent(
    title: String,
    onHistoryDropdown: @Composable () -> Unit,
    onMoreDropdown: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) =
    LargeTopAppBar(
        title = {
            Text(
                text = title
            )
        },
        actions = {
            Box {
                var isHistoryClicked by remember { mutableStateOf(false) }
                IconButton(onClick = { isHistoryClicked = !isHistoryClicked }) {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = "History"
                    )
                }
                if (isHistoryClicked) {
                    onHistoryDropdown()
                }
            }
            Box {
                var isMoreClicked by remember { mutableStateOf(false) }
                IconButton(onClick = { isMoreClicked = !isMoreClicked }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "More"
                    )
                }
                if (isMoreClicked) {
                    onMoreDropdown()
                }
            }
        },
        scrollBehavior = scrollBehavior
    )

@Preview
@Composable
fun TopAppBarComponentPreview() {
    LibraryScreen()
}