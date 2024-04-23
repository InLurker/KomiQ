package com.inlurker.komiq.viewmodel.utils

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