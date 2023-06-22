package com.inlurker.komiq.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SortingToolbar(
    sortingMethods: List<String>,
    onSortingMethodSelected: (Int, String) -> Unit,
    onAscendingClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedDropdownBox(
            items = sortingMethods,
            selectedItem = sortingMethods[0],
            onItemSelected = onSortingMethodSelected
        )
        IconButton(
            onClick = onAscendingClicked,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDownward,
                contentDescription = "Ascending",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onFilterClicked,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}