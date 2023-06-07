package com.inlurker.komiq.ui.screens.helper


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun adjustTone(baseColor: Color, percentage: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(baseColor.toArgb(), hsl)
    val luminance = percentage * hsl[2]
    val rgb = ColorUtils.HSLToColor(floatArrayOf(hsl[0], hsl[1], luminance))
    return Color(rgb)
}
