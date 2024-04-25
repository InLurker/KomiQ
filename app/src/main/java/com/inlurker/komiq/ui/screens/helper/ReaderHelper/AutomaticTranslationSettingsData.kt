package com.inlurker.komiq.ui.screens.helper.ReaderHelper

import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.model.translation.targetlanguages.TargetLanguage
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextRecognition
import com.inlurker.komiq.ui.screens.helper.Enumerated.TranslationEngine

data class AutomaticTranslationSettingsData(
    var enabled: Boolean,
    var sourceLanguage: ComicLanguageSetting,
    var textDetection: TextDetection,
    var textRecognition: TextRecognition,
    var translationEngine: TranslationEngine,
    var targetLanguage: TargetLanguage
)