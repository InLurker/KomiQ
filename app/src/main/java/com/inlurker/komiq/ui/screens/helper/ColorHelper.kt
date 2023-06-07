package com.inlurker.komiq.ui.screens.helper


import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils

fun generateMonochromaticPalette(baseColor: Int, numShades: Int): List<Color> {
    val palette = mutableListOf<Color>()

    // Convert the base color to HSL
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(baseColor, hsl)

    val baseHue = hsl[0]
    val baseSaturation = hsl[1]
    val baseLightness = hsl[2]

    // Calculate the step size for each shade
    val step = (1f - baseLightness) / (numShades - 1)

    // Generate the shades of the color
    for (i in 0 until numShades) {
        val lightness = baseLightness + (step * i)
        val shade = ColorUtils.HSLToColor(floatArrayOf(baseHue, baseSaturation, lightness))
        palette.add(Color(shade))
    }
    return palette
}
