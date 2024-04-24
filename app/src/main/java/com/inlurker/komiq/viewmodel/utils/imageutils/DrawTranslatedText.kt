package com.inlurker.komiq.viewmodel.utils.imageutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.inlurker.komiq.R
import com.inlurker.komiq.model.data.boundingbox.BoundingBox


@RequiresApi(Build.VERSION_CODES.Q)
fun drawTranslatedText(context: Context, inputImage: Bitmap, textBoundingBoxPairs: List<Pair<BoundingBox, String>>): Bitmap {
    val outputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outputImage)
    val backgroundPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
        typeface = ResourcesCompat.getFont(context, R.font.wildwords)
    }

    textBoundingBoxPairs.forEach { (box, text) ->
        // Fill the bounding box with white
        canvas.drawRect(box.X1, box.Y1, box.X2, box.Y2, backgroundPaint)

        var textSize = 30f
        var staticLayout: StaticLayout
        val targetWidth = ((box.X2 - box.X1) * 0.9).toInt() // 90% of bounding box width
        val targetHeight = (box.Y2 - box.Y1) * 0.7 // 70% of bounding box height

        do {
            textPaint.textSize = textSize

            val builder = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, targetWidth)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL)
                .setBreakStrategy(LineBreaker.BREAK_STRATEGY_BALANCED)

            staticLayout = builder.build()

            // Use the targetHeight to check if the StaticLayout's height exceeds 80% of the bounding box height
            if (staticLayout.height <= targetHeight || textSize == 15f) {
                break
            }
            textSize--
        } while (textSize >= 15f)

        // Center the text in the bounding box taking into account the reduced target width and height
        val xCoordinate = box.X1 + (box.X2 - box.X1 - targetWidth) / 2f
        val yCoordinate = box.Y1 + (box.Y2 - box.Y1 - staticLayout.height) / 2f

        // Save the canvas state
        canvas.save()
        canvas.translate(xCoordinate, yCoordinate)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    return outputImage
}