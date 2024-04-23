package com.inlurker.komiq.ui.screens.helper.ReaderHelper

import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextRecognition
import com.inlurker.komiq.ui.screens.helper.Enumerated.TranslationEngine

data class AutomaticTranslationSettingsData(
    val enabled: Boolean,
    val sourceLanguage: ComicLanguageSetting,
    val textDetection: TextDetection,
    val textRecognition: TextRecognition,
    val translationEngine: TranslationEngine
)