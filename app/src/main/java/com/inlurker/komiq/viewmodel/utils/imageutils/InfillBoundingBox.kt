package com.inlurker.komiq.viewmodel.utils.imageutils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.inlurker.komiq.model.data.boundingbox.BoundingBox

fun infillBoundingBox(canvas: Canvas, boundingBox: BoundingBox, colorInt: Int) {
    val paint = Paint().apply {
        style = Paint.Style.FILL
        color = colorInt
    }
    canvas.drawRect(RectF(boundingBox.X1, boundingBox.Y1, boundingBox.X2, boundingBox.Y2), paint)
}