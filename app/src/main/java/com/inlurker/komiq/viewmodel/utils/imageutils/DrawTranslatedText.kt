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
    val inpaintBoxes = mutableListOf<BoundingBox>()
    val infillBoxes = mutableListOf<Pair<BoundingBox, Int>>()
    var outputBitmap = inputImage.copy(Bitmap.Config.ARGB_8888, true)

    textBoundingBoxPairs.forEach { (box, _) ->
        val eval = shouldUseSingleColorFill(outputBitmap, box)
        if (eval.first) {
            infillBoxes += Pair(box, eval.second)
        } else inpaintBoxes += box
    }

    val canvas = Canvas(outputBitmap)
    if (infillBoxes.isNotEmpty()) {
        infillBoxes.forEach { (box, color) ->
            // Fill the bounding box with white
            infillBoundingBox(canvas, box, color)
        }
    }

    if (inpaintBoxes.isNotEmpty()) {
        outputBitmap = inpaintBitmap(outputBitmap, inpaintBoxes)
    }

    val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
        typeface = Typeface.create(ResourcesCompat.getFont(context, R.font.animeace), Typeface.BOLD)
    }
    drawTextInBox(outputBitmap, textPaint, textBoundingBoxPairs)

    return outputBitmap
}


@RequiresApi(Build.VERSION_CODES.Q)
fun drawTranslatedTextVertical(fontSize: Float, inputImage: Bitmap, textBoundingBoxPairs: List<Pair<BoundingBox, String>>): Bitmap {
    // Create a copy of the input image in the correct format
    val inpaintBoxes = mutableListOf<BoundingBox>()
    val infillBoxes = mutableListOf<Pair<BoundingBox, Int>>()
    var outputBitmap = inputImage.copy(Bitmap.Config.ARGB_8888, true)

    textBoundingBoxPairs.forEach { (box, _) ->
        val eval = shouldUseSingleColorFill(outputBitmap, box)
        if (eval.first) {
            infillBoxes += Pair(box, eval.second)
        } else inpaintBoxes += box
    }

    val canvas = Canvas(outputBitmap)
    if (infillBoxes.isNotEmpty()) {
        infillBoxes.forEach { (box, color) ->
            // Fill the bounding box with white
            infillBoundingBox(canvas, box, color)
        }
    }

    if (inpaintBoxes.isNotEmpty()) {
        outputBitmap = inpaintBitmap(outputBitmap, inpaintBoxes)
    }

    drawVerticalText(outputBitmap, fontSize, textBoundingBoxPairs,)

    return outputBitmap
}

fun drawTextInBox(inputImage: Bitmap, textPaint: TextPaint, textBoundingBoxPairs: List<Pair<BoundingBox, String>>) {
    val canvas = Canvas(inputImage)

    val strokePaint = TextPaint(textPaint).apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f // Adjust stroke width to your preference
        color = Color.WHITE
    }
    textBoundingBoxPairs.forEach { (box, text) ->
        var textSize = 33f
        var staticLayout: StaticLayout
        val targetWidth = ((box.X2 - box.X1) * 0.9).toInt() // 90% of bounding box width
        val targetHeight = (box.Y2 - box.Y1) * 0.8 // 70% of bounding box height

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

        strokePaint.textSize = textSize
        val strokeBuilder = StaticLayout.Builder.obtain(text, 0, text.length, strokePaint, targetWidth)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL)
            .setBreakStrategy(LineBreaker.BREAK_STRATEGY_HIGH_QUALITY).build()

        // Center the text in the bounding box taking into account the reduced target width and height
        val xCoordinate = box.X1 + (box.X2 - box.X1 - targetWidth) / 2f
        val yCoordinate = box.Y1 + (box.Y2 - box.Y1 - staticLayout.height) / 2f

        // Save the canvas state
        canvas.save()
        canvas.translate(xCoordinate, yCoordinate)
        strokeBuilder.draw(canvas)
        staticLayout.draw(canvas)
        canvas.restore()
    }
}

fun drawVerticalText(inputImage: Bitmap, fontSize: Float, textBoundingBoxPairs: List<Pair<BoundingBox, String>>) {
    val canvas = Canvas(inputImage)
    val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = fontSize
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    // Set up the stroke paint
    val strokePaint = Paint(textPaint).apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f // Adjust the stroke width according to your preference
        color = Color.WHITE
    }


    val fontHeight = calculateFontHeight(fontSize)

    textBoundingBoxPairs.forEach { (box, text) ->
        val columnsNeeded =
            Math.ceil(text.length * fontHeight.toDouble() / (box.Y2 - box.Y1)).toInt()
        val columnWidth = (box.X2 - box.X1) / columnsNeeded - 5

        var textPosX = box.X2 - columnWidth / 2 - 3
        var textPosY = box.Y1
        var currentColumnHeight = 0

        for (char in text.toVerticalPunctuation()) {
            if (char == '\n' || currentColumnHeight + fontHeight > box.Y2 - box.Y1) {
                textPosX -= (columnWidth + 5)
                textPosY = box.Y1
                currentColumnHeight = 0
                if (textPosX < box.X1) break
                if (char == '\n') continue
            }
            canvas.drawText(char.toString(), textPosX, textPosY + fontHeight, strokePaint)
            canvas.drawText(char.toString(), textPosX, textPosY + fontHeight, textPaint)

            textPosY += fontHeight
            currentColumnHeight += fontHeight
        }
    }
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