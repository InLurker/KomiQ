package com.inlurker.komiq.ui.screens.helper.Enumerated

import com.inlurker.komiq.model.data.repository.ComicLanguageSetting

enum class TextRecognition(val description: String) {
    MLKit("MLKit"),
    MangaOCR("MangaOCR"),
    PADDLEOCR("Paddle OCR");

    companion object {
        fun getOptionList(languageSetting: ComicLanguageSetting): List<TextRecognition> {
            return when (languageSetting) {
                ComicLanguageSetting.Japanese -> TextRecognition.values().toList()
                ComicLanguageSetting.Chinese,
                ComicLanguageSetting.Korean,
                ComicLanguageSetting.Arabic,
                ComicLanguageSetting.French,
                ComicLanguageSetting.German,
                ComicLanguageSetting.English -> listOf(
                    MLKit,
                    PADDLEOCR
                )
                else -> listOf(MLKit)
            }
        }
    }
}
