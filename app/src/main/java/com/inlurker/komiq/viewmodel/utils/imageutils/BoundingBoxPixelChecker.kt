package com.inlurker.komiq.viewmodel.utils.imageutils

import android.graphics.Bitmap
import android.graphics.Color
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun shouldUseSingleColorFill(bitmap: Bitmap, box: BoundingBox, tolerance: Int = 10): Pair<Boolean, Int> {
    val x1 = max(0, box.X1.toInt() - 1)
    val y1 = max(0, box.Y1.toInt() - 1)
    val x2 = min(bitmap.width, box.X2.toInt() + 1)
    val y2 = min(bitmap.height, box.Y2.toInt() + 1)

    val totalPixels = (x2 - x1) * (y2 - y1)
    var similarPixels = 0

    var sumR = 0
    var sumG = 0
    var sumB = 0

    val initialColor = bitmap.getPixel(x1, y1)
    for (x in x1 until x2) {
        for (y in y1 until y2) {
            val currentColor = bitmap.getPixel(x, y)
            if (isColorClose(initialColor, currentColor, tolerance)) {
                similarPixels++
                sumR += Color.red(currentColor)
                sumG += Color.green(currentColor)
                sumB += Color.blue(currentColor)
            }
        }
    }

    val averageColor = if (similarPixels > 0) {
        Color.rgb(sumR / similarPixels, sumG / similarPixels, sumB / similarPixels)
    } else {
        initialColor // If no similar pixels found, return the color of the initial pixel
    }

    val similarityPercentage = similarPixels.toDouble() / totalPixels

    return Pair(similarityPercentage >= 0.6, averageColor)
}

fun isColorClose(color1: Int, color2: Int, tolerance: Int): Boolean {
    val r1 = Color.red(color1)
    val g1 = Color.green(color1)
    val b1 = Color.blue(color1)
    val r2 = Color.red(color2)
    val g2 = Color.green(color2)
    val b2 = Color.blue(color2)
    return abs(r1 - r2) < tolerance && abs(g1 - g2) < tolerance && abs(b1 - b2) < tolerance
}