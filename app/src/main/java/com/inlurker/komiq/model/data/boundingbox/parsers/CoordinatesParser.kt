package com.inlurker.komiq.model.data.boundingbox.parsers

import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import kotlin.math.ceil

fun coordinatesToBoundingBox(boxes: Array<Array<FloatArray>>): List<BoundingBox> {
    // Initialize a mutable list to hold the results
    val coordinatesList = mutableListOf<BoundingBox>()

    // Process each box
    for (box in boxes) {
        val x1 = box.minOf { it[0] }
        val y1 = box.minOf { it[1] }
        val x2 = box.maxOf { it[0] }
        val y2 = box.maxOf { it[1] }

        // Round up the coordinates to the nearest integer
        val roundedX1 = ceil(x1)
        val roundedY1 = ceil(y1)
        val roundedX2 = ceil(x2)
        val roundedY2 = ceil(y2)

        // Create a RoundedBoxCoordinates object and add it to the list
        coordinatesList.add(BoundingBox(roundedX1, roundedY1, roundedX2, roundedY2))
    }

    // Return the list of rounded coordinates
    return coordinatesList
}

