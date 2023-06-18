package com.inlurker.komiq.ui.screens.helper


import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette

fun adjustLuminance(baseColor: Color, luminance: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(baseColor.toArgb(), hsl)
    val rgb = ColorUtils.HSLToColor(floatArrayOf(hsl[0], hsl[1], luminance))
    return Color(rgb)
}

fun generateColorPalette(bitmap: Bitmap): Palette {
    return Palette.Builder(bitmap).generate()
}