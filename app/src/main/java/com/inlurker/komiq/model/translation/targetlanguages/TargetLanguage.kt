package com.inlurker.komiq.model.translation.targetlanguages

interface TargetLanguage {
    val isoCode: String
    val languageName: String
}

fun getLanguageList(targetLanguages: TargetLanguage): List<TargetLanguage> {
    return when (targetLanguages) {
        is DeepLTargetLanguage -> DeepLTargetLanguage.values().toList()
        is GoogleTLTargetLanguage -> GoogleTLTargetLanguage.values().toList()
        else -> throw IllegalArgumentException("Unsupported language type")
    }
}