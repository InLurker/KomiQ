package com.inlurker.komiq.model.data.boundingbox.parsers

import com.inlurker.komiq.model.data.boundingbox.BoundingBox

fun combineAndConvertBoxes(boxes: Array<Array<FloatArray>>): List<BoundingBox> {
    val result = mutableListOf<BoundingBox>()

    // Sort and prepare boxes for merging
    val sortedBoxes = boxes.map { box ->
        val x1 = box.minOf { it[0] }
        val y1 = box.minOf { it[1] }
        val x2 = box.maxOf { it[0] }
        val y2 = box.maxOf { it[1] }
        arrayOf(floatArrayOf(x1, y1), floatArrayOf(x2, y2))
    }.sortedWith(compareBy({ it[0][0] }, { it[0][1] }))

    // Start with the first box as the current box to compare others against
    var current = sortedBoxes[0]
    for (box in sortedBoxes) {
        if (isInside(current, box)) {
            // If box is inside, extend the current bounding box if necessary
            current[1][0] = maxOf(current[1][0], box[1][0])
            current[1][1] = maxOf(current[1][1], box[1][1])
        } else {
            // Add the current box as a BoundingBox to the result list
            result.add(createBoundingBox(current))
            current = box
        }
    }
    // Add the last processed box
    result.add(createBoundingBox(current))

    return result
}
fun isInside(outerBox: Array<FloatArray>, innerBox: Array<FloatArray>): Boolean {
    return innerBox[0][0] >= outerBox[0][0] && innerBox[0][1] >= outerBox[0][1] &&
            innerBox[1][0] <= outerBox[1][0] && innerBox[1][1] <= outerBox[1][1]
}

fun createBoundingBox(box: Array<FloatArray>): BoundingBox {
    // You may round these values if necessary
    val x1 = box[0][0]
    val y1 = box[0][1]
    val x2 = box[1][0]
    val y2 = box[1][1]
    return BoundingBox(x1, y1, x2, y2)
}