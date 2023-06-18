package com.inlurker.komiq.ui.screens.helper

import java.text.DecimalFormat

fun removeTrailingZero(float: Float): String {
    val format = DecimalFormat("0.#")
    return format.format(float)
}