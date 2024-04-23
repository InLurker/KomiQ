package com.inlurker.komiq.ui.screens.helper.Enumerated

enum class TextDetection(val description: String) {
    CRAFT("CRAFT"),
    MLKit("MLKit");

    companion object {
        fun getOptionList(): List<TextDetection> {
            return listOf(CRAFT, MLKit)
        }
    }
}
