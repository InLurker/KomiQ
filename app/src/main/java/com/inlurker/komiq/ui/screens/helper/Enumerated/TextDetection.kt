package com.inlurker.komiq.ui.screens.helper.Enumerated

import com.inlurker.komiq.model.data.repository.ComicLanguageSetting

enum class TextDetection(val description: String) {
    MLKit("MLKit"),
    CRAFT("CRAFT"),
    PADDLEOCR("Paddle OCR");

    companion object {
        fun getOptionList(languageSetting: ComicLanguageSetting): List<TextDetection> {
            return when (languageSetting) {
                ComicLanguageSetting.Japanese,
                ComicLanguageSetting.Chinese,
                ComicLanguageSetting.Korean,
                ComicLanguageSetting.Arabic,
                ComicLanguageSetting.French,
                ComicLanguageSetting.German,
                ComicLanguageSetting.English -> TextDetection.values().toList()
                else -> listOf(MLKit, CRAFT)
            }
        }
    }
}
