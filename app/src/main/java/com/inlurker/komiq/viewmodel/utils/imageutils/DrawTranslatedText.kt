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
        typeface = ResourcesCompat.getFont(context, R.font.animeace)
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
                .setBreakStrategy(LineBreaker.BREAK_STRATEGY_HIGH_QUALITY)

            staticLayout = builder.build()

            // Use the targetHeight to check if the StaticLayout's height exceeds 80% of the bounding box height
            if (staticLayout.height <= targetHeight || textSize == 15f) {
                break
            }
            textSize -= 3
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

@RequiresApi(Build.VERSION_CODES.Q)
fun drawTranslatedTextVertical(context: Context, inputImage: Bitmap, textBoundingBoxPairs: List<Pair<BoundingBox, String>>): Bitmap {
    val outputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outputImage)
    val backgroundPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 35f  // Example font size, adjustable as needed
    }

    val fontMetrics = textPaint.fontMetrics
    val fontHeight = Math.ceil((fontMetrics.descent - fontMetrics.top) * 0.9).toInt()
    val columnWidth = Math.max(fontHeight, 38)  // Reasonable column width

    textBoundingBoxPairs.forEach { (box, text) ->
        canvas.drawRect(box.X1, box.Y1, box.X2, box.Y2, backgroundPaint)

        // Determine how many columns of text will be needed
        val totalHeightRequired = text.count { it != '\n' } * fontHeight
        val columnsNeeded = Math.ceil(totalHeightRequired.toDouble() / (box.Y2 - box.Y1)).toInt()
        val totalColumnWidth = columnWidth * columnsNeeded

        // Start drawing from the rightmost point of the bounding box, moving left for each new column
        var textPosX = (box.X1 + box.X2) / 2 + totalColumnWidth / 2 - columnWidth / 2
        var textPosY = box.Y1
        var currentColumnHeight = 0
        var lastCharWasNewLine = false

        for (char in text.toVerticalPunctuation()) {
            if (char == '\n' || currentColumnHeight + fontHeight > box.Y2 - box.Y1) {
                if (!lastCharWasNewLine) { // Move to the previous column if the last character was not a newline
                    textPosX -= columnWidth
                    currentColumnHeight = 0
                }
                textPosY = box.Y1
                lastCharWasNewLine = char == '\n'
                if (textPosX - columnWidth < box.X1) break // Stop drawing if we run out of horizontal space
            } else {
                lastCharWasNewLine = false
                canvas.drawText(char.toString(), textPosX, textPosY + fontHeight, textPaint)
                textPosY += fontHeight
                currentColumnHeight += fontHeight
            }
        }
    }

    return outputImage
}

//also help me convert them all from char to string from single quote to double quote ' to "
val punctuationMap = mapOf(
    ',' to "、",
    '!' to "！",
    '?' to "？",
    ':' to "：",
    ';' to "；",
    '"' to "〝",
    '\'' to "〞",
    '(' to "（",
    ')' to "）",
    '[' to "［",
    ']' to "］",
    '{' to "｛",
    '}' to "｝",
    '…' to "︙",
    '「' to "﹁",
    '」' to "」",
    '《' to "︽",
    '》' to "︾",
    '〈' to "︿",
    '〉' to "﹀",
    '﹃' to "﹃",
    '﹄' to "﹄"
)


fun String.toVerticalPunctuation(): String {
    val stringBuilder = StringBuilder()
    this.forEach { char ->
        val verticalChar = punctuationMap[char]
        if (verticalChar != null) {
            stringBuilder.append(verticalChar)
        } else {
            stringBuilder.append(char)
        }
    }
    return stringBuilder.toString()
}