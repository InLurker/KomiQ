package com.inlurker.komiq.model.data.boundingbox.parsers

fun combineCoordinates(boxes: Array<Array<FloatArray>>): Array<Array<FloatArray>> {
    val result = mutableListOf<Array<FloatArray>>()

    // Step 1: Sort and filter overlapping boxes
    val sortedBoxes = boxes.map { box ->
        val x1 = box.minOf { it[0] }
        val y1 = box.minOf { it[1] }
        val x2 = box.maxOf { it[0] }
        val y2 = box.maxOf { it[1] }
        arrayOf(floatArrayOf(x1, y1), floatArrayOf(x2, y2))
    }.sortedWith(compareBy({ it[0][0] }, { it[0][1] })) // Sort by x1 then y1

    // Step 2: Merge boxes
    var current = sortedBoxes[0]
    for (box in sortedBoxes) {
        if (isInside(current, box)) {
            // Extend the current box if necessary
            current[1][0] = maxOf(current[1][0], box[1][0])
            current[1][1] = maxOf(current[1][1], box[1][1])
        } else {
            // Add the current box to result and update current to new box
            result.add(current)
            current = box
        }
    }
    result.add(current) // Add the last box

    return result.toTypedArray()
}

fun isInside(outerBox: Array<FloatArray>, innerBox: Array<FloatArray>): Boolean {
    return innerBox[0][0] >= outerBox[0][0] && innerBox[0][1] >= outerBox[0][1] &&
            innerBox[1][0] <= outerBox[1][0] && innerBox[1][1] <= outerBox[1][1]
}