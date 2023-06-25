package com.inlurker.komiq.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.RotatingIconButton

@Composable
fun SortingToolbar(
    sortingMethods: List<String>,
    onSortingMethodSelected: (Int, String) -> Unit,
    isSortingOrderDescending: Boolean,
    onSortingOrderClicked: (Boolean) -> Unit,
    onFilterClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                enabled = true,
                onClick = onFilterClicked
            )
    ) {
        RoundedDropdownBox(
            items = sortingMethods,
            selectedItem = sortingMethods[0],
            onItemSelected = onSortingMethodSelected
        )
        RotatingIconButton(
            isRotated = isSortingOrderDescending,
            onClick = { toggleResult ->
                onSortingOrderClicked(toggleResult)
            },
            modifier = Modifier
                .size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDownward,
                contentDescription = "Sorting Order Button",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.FilterList,
            contentDescription = "Filter",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent)
        )
    }
}