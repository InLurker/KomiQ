package com.inlurker.komiq.viewmodel.utils.imageutils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.inlurker.komiq.model.data.boundingbox.BoundingBox

fun drawBoundingBoxes(inputImage: Bitmap, boxes: List<BoundingBox>): Bitmap {
    // Create a mutable copy of the input image to draw on
    val outputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outputImage)
    val paint = Paint()

    // Set the color and style for the bounding boxes
    paint.color = Color.RED
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3f

    boxes.forEach { box ->
        canvas.drawRect(box.X1, box.Y1, box.X2, box.Y2, paint)
    }

    return outputImage
}