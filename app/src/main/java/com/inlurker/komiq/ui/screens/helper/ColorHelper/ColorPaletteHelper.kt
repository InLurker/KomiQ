package com.inlurker.komiq.ui.screens.helper.ColorHelper


import android.graphics.Bitmap
import androidx.palette.graphics.Palette

fun generateColorPalette(bitmap: Bitmap): Palette {
    return Palette.Builder(bitmap).generate()
}