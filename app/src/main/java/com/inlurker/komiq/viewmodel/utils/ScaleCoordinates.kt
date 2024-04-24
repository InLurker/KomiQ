package com.inlurker.komiq.viewmodel.utils

import com.inlurker.komiq.model.data.boundingbox.BoundingBox

fun scaleCoordinates(coords: Array<Array<FloatArray>>, originalSize: Pair<Int, Int>, targetSize: Pair<Int, Int>): Array<Array<FloatArray>> {
    val (originalWidth, originalHeight) = originalSize
    val (targetWidth, targetHeight) = targetSize

    val newWidth = targetWidth.toFloat()
    val newHeight  = targetHeight.toFloat()

    val ratioW = newWidth / originalWidth
    val ratioH = newHeight / originalHeight

    return coords.map { arr ->
        arr.map { point ->
            floatArrayOf((point[0] * ratioW).coerceIn(0f, newWidth), (point[1] * ratioH).coerceIn(0f, newHeight))
        }.toTypedArray()
    }.toTypedArray()
}

fun scaleBoundingBoxes(boundingBoxes: List<BoundingBox>, originalSize: Pair<Int, Int>, targetSize: Pair<Int, Int>): List<BoundingBox> {
    val (originalWidth, originalHeight) = originalSize
    val (targetWidth, targetHeight) = targetSize

    val newWidth = targetWidth.toFloat()
    val newHeight  = targetHeight.toFloat()
    val ratioW = newWidth/ originalWidth
    val ratioH = newHeight / originalHeight

    return boundingBoxes.map { bbox ->
        BoundingBox(
            X1 = (bbox.X1 * ratioW).coerceIn(0f, newWidth),
            Y1 = (bbox.Y1 * ratioH).coerceIn(0f, newHeight),
            X2 = (bbox.X2 * ratioW).coerceIn(0f, newWidth),
            Y2 = (bbox.Y2 * ratioH).coerceIn(0f, newHeight)
        )
    }
}