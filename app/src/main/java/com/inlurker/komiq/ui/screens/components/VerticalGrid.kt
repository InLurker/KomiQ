
package com.example.jetsnack.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/**
 * A simple grid which lays elements out vertically in evenly sized [columns].
 */
@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / columns
        // Keep given height constraints, but set an exact width
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )
        // Measure each item with these constraints
        val placeables = measurables.map { it.measure(itemConstraints) }
        // Track each column's height so we can calculate the overall height
        val columnHeights = Array(columns) { 0 }
        placeables.forEachIndexed { index, placeable ->
            val column = index % columns
            columnHeights[column] += placeable.height
        }
        val height = (columnHeights.maxOrNull() ?: constraints.minHeight)
            .coerceAtMost(constraints.maxHeight)
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            // Track the Y co-ordinate per column we have placed up to
            val columnY = Array(columns) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val column = index % columns
                val x = column * itemWidth
                val y = columnY[column]
                placeable.placeRelative(x, y)
                columnY[column] += placeable.height
            }
        }
    }
}