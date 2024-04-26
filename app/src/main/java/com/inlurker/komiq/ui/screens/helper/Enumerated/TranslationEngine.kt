package com.inlurker.komiq.ui.screens.helper.Enumerated

enum class TranslationEngine(val description: String) {
    Google("Google Translate"),
    DeepL("DeepL Translate"),
    Caiyun("Caiyun Translate"),
    Gemini("Gemini AI");

    companion object {
        fun getOptionList(): List<TranslationEngine> {
            return TranslationEngine.values().toList()
        }
    }
}
