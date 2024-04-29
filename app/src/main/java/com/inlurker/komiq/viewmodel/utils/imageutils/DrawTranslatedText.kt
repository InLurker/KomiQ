package com.inlurker.komiq.viewmodel.utils.imageutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
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
fun drawTranslatedTextVertical(fontSize: Float, inputImage: Bitmap, textBoundingBoxPairs: List<Pair<BoundingBox, String>>): Bitmap {
    val outputImage = inputImage.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(outputImage)
    val backgroundPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    textBoundingBoxPairs.forEach { (box, text) ->
        canvas.drawRect(box.X1, box.Y1, box.X2, box.Y2, backgroundPaint)

        // Dynamically calculate the font size based on the bounding box size and text length
        val fontHeight = calculateFontHeight(fontSize)
        val columnsNeeded = Math.ceil(text.length * fontHeight.toDouble() / (box.Y2 - box.Y1)).toInt()
        val columnWidth = (box.X2 - box.X1) / columnsNeeded - 5 // subtract gap between columns

        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = fontSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set typeface to semibold
        }

        // Start drawing from the rightmost point of the bounding box, moving left for each new column
        var textPosX = box.X2 - columnWidth / 2 - 3 // Start from the right with a gap
        var textPosY = box.Y1
        var currentColumnHeight = 0

        for (char in text.toVerticalPunctuation()) {
            if (char == '\n' || currentColumnHeight + fontHeight > box.Y2 - box.Y1) {
                textPosX -= (columnWidth + 5) // Move to the next column with a gap
                textPosY = box.Y1
                currentColumnHeight = 0
                if (textPosX < box.X1) break // Stop drawing if we run out of horizontal space
                if (char == '\n') continue // Skip further processing for newline character itself
            }
            canvas.drawText(char.toString(), textPosX, textPosY + fontHeight, textPaint)
            textPosY += fontHeight
            currentColumnHeight += fontHeight
        }
    }

    return outputImage
}


fun calculateFontHeight(fontSize: Float): Int {
    val paint = Paint()
    paint.textSize = fontSize
    val fm = paint.fontMetrics
    return Math.ceil((fm.descent - fm.ascent) * 0.9).toInt()
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
    '『' to "﹃",
    '』' to "﹄"
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