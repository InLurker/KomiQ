package com.inlurker.komiq.model.data.boundingbox.parsers

import com.inlurker.komiq.model.data.boundingbox.BoundingBox

fun correctBoundingBoxes(boundingBoxes: List<BoundingBox>, width: Int, height: Int): List<BoundingBox> {
    return boundingBoxes.map { bbox ->
        BoundingBox(
            X1 = bbox.X1.coerceIn(0f, width.toFloat()),
            Y1 = bbox.Y1.coerceIn(0f, height.toFloat()),
            X2 = bbox.X2.coerceIn(0f, width.toFloat()),
            Y2 = bbox.Y2.coerceIn(0f, height.toFloat())
        )
    }
}