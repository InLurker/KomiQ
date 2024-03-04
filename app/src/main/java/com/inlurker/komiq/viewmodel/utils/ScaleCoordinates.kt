package com.inlurker.komiq.viewmodel.utils

fun scaleCoordinates(coords: Array<Array<FloatArray>>, originalSize: Pair<Int, Int> = Pair(300, 400), targetSize: Pair<Int, Int>): Array<Array<FloatArray>> {
    val (originalWidth, originalHeight) = originalSize
    val (targetWidth, targetHeight) = targetSize

    return coords.map { area ->
        area.map { point ->
            floatArrayOf(
                (point[0] / originalWidth) * targetWidth,
                (point[1] / originalHeight) * targetHeight
            )
        }.toTypedArray()
    }.toTypedArray()
}