package com.inlurker.komiq.ui.screens.helper.ReaderHelper

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

fun calculateColorFilterMatrix(
    eyeCare: Int,
    brightnessScale: Int,
    saturationScale: Int,
    isGreyscaleSelected: Boolean,
    isInvertSelected: Boolean
): ColorFilter {
    val result = ColorMatrix()

    val brightness = (brightnessScale / 100f) * 0.8f + 0.2f
    val warmness = eyeCare / 100f
    val saturation = saturationScale / 100f

    // Adjust brightness
    result.setToScale(
        brightness,
        brightness,
        brightness,
        1f
    )

    // Adjust eyeCare Filter
    val eyeCareMatrix =
        ColorMatrix().apply {
            setToScale(
                1f,
                1f - (warmness / 3),
                1f - warmness,
                1f
            )
        }

    val saturationMatrix =
        ColorMatrix().apply {
            setToSaturation(saturation)
        }

    result *= eyeCareMatrix
    result *= saturationMatrix

    if(isGreyscaleSelected) {
        result *= ColorMatrix().apply {
            setToSaturation(0f)
        }
    }
    if(isInvertSelected) {
        result *= ColorMatrix(
            floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    }

    return ColorFilter.colorMatrix(
        colorMatrix = result
    )
}