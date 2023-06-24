package com.inlurker.komiq.ui.screens.helper.ColorHelper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun adjustLuminance(baseColor: Color, luminance: Float): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(baseColor.toArgb(), hsl)
    val rgb = ColorUtils.HSLToColor(floatArrayOf(hsl[0], hsl[1], luminance))
    return Color(rgb)
}