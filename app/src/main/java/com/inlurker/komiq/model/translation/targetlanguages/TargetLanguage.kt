package com.inlurker.komiq.model.translation.targetlanguages

import com.inlurker.komiq.model.translation.targetlanguages.caiyun.CaiyunENTargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.caiyun.CaiyunJATargetLanguage
import com.inlurker.komiq.model.translation.targetlanguages.caiyun.CaiyunZHTargetLanguage

interface TargetLanguage {
    val isoCode: String
    val languageName: String
}

fun getLanguageList(targetLanguages: TargetLanguage): List<TargetLanguage> {
    return when (targetLanguages) {
        is DeepLTargetLanguage -> DeepLTargetLanguage.values()
        is GoogleTLTargetLanguage -> GoogleTLTargetLanguage.values()
        is CaiyunENTargetLanguage -> CaiyunENTargetLanguage.values()
        is CaiyunZHTargetLanguage -> CaiyunZHTargetLanguage.values()
        is CaiyunJATargetLanguage -> CaiyunJATargetLanguage.values()
        else -> throw IllegalArgumentException("Unsupported language type")
    }.toList()
}