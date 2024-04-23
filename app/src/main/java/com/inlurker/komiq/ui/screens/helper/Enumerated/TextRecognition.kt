package com.inlurker.komiq.ui.screens.helper.Enumerated

import com.inlurker.komiq.model.data.repository.ComicLanguageSetting

enum class TextRecognition(val description: String) {
    MangaOCR("MangaOCR"),
    MLKit("MLKit");

    companion object {
        fun getOptionList(languageSetting: ComicLanguageSetting): List<TextRecognition> {
            return if (languageSetting == ComicLanguageSetting.Japanese)
                listOf(MangaOCR, MLKit)
            else listOf(MLKit)
        }
    }
}
