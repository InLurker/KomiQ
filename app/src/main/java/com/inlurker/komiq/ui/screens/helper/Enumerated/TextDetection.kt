package com.inlurker.komiq.ui.screens.helper.Enumerated

import com.inlurker.komiq.model.data.repository.ComicLanguageSetting

enum class TextDetection(val description: String) {
    CRAFT("CRAFT"),
    PADDLEOCR("Paddle OCR"),
    MLKit("MLKit");

    companion object {
        fun getOptionList(languageSetting: ComicLanguageSetting): List<TextDetection> {
            return when (languageSetting) {
                ComicLanguageSetting.Japanese -> TextDetection.values().toList()
                ComicLanguageSetting.Chinese,
                ComicLanguageSetting.Korean,
                ComicLanguageSetting.Arabic,
                ComicLanguageSetting.French,
                ComicLanguageSetting.German,
                ComicLanguageSetting.English -> listOf(
                    PADDLEOCR,
                    MLKit
                )
                else -> listOf(MLKit)
            }
        }
    }
}
