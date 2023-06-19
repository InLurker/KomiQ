package com.inlurker.komiq.ui.screens.helper

import java.text.DecimalFormat

fun removeTrailingZero(float: Float): String {
    val format = DecimalFormat("0.#")
    return format.format(float)
}

fun pluralize(value: Int, unit: String): String {
    val pluralUnit = if (value > 1) unit + "s" else unit
    return "$value $pluralUnit"
}