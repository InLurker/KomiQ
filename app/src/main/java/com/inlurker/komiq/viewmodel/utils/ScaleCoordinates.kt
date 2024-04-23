package com.inlurker.komiq.viewmodel.utils

import com.inlurker.komiq.model.data.boundingbox.BoundingBox

fun scaleCoordinates(coords: Array<Array<FloatArray>>, originalSize: Pair<Int, Int>, targetSize: Pair<Int, Int>): Array<Array<FloatArray>> {
    val (originalWidth, originalHeight) = originalSize
    val (targetWidth, targetHeight) = targetSize


    val ratioW = targetWidth.toFloat() / originalWidth
    val ratioH = targetHeight.toFloat() / originalHeight

    return coords.map { arr ->
        arr.map { point ->
            floatArrayOf(point[0] * ratioW, point[1] * ratioH)
        }.toTypedArray()
    }.toTypedArray()
}

fun scaleBoundingBoxes(boundingBoxes: List<BoundingBox>, originalSize: Pair<Int, Int>, targetSize: Pair<Int, Int>): List<BoundingBox> {
    val (originalWidth, originalHeight) = originalSize
    val (targetWidth, targetHeight) = targetSize

    val ratioW = targetWidth.toFloat() / originalWidth
    val ratioH = targetHeight.toFloat() / originalHeight

    return boundingBoxes.map { bbox ->
        BoundingBox(
            X1 = bbox.X1 * ratioW,
            Y1 = bbox.Y1 * ratioH,
            X2 = bbox.X2 * ratioW,
            Y2 = bbox.Y2 * ratioH
        )
    }
}