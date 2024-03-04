package com.inlurker.komiq.viewmodel.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.ceil

fun drawBoundingBoxes(inputImage: Bitmap, boxes: Array<Array<FloatArray>>): Bitmap {
    // Create a mutable copy of the input image to draw on
    val outputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outputImage)
    val paint = Paint()

    // Set the color and style for the bounding boxes
    paint.color = Color.RED
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3f

    // Draw each bounding box on the image
    for (box in boxes) {
        val x1 = box.minOf { it[0] }
        val y1 = box.minOf { it[1] }
        val x2 = box.maxOf { it[0] }
        val y2 = box.maxOf { it[1] }

        // Round up the coordinates to the nearest integer
        val roundedX1 = ceil(x1).toInt()
        val roundedY1 = ceil(y1).toInt()
        val roundedX2 = ceil(x2).toInt()
        val roundedY2 = ceil(y2).toInt()

        canvas.drawRect(roundedX1.toFloat(), roundedY1.toFloat(), roundedX2.toFloat(), roundedY2.toFloat(), paint)
    }

    return outputImage
}

fun resizeImage(bitmap: Bitmap): Bitmap {
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true)

    return resizedBitmap
}