package com.inlurker.komiq.model.translation.targetlanguages.caiyun

import com.inlurker.komiq.model.translation.targetlanguages.TargetLanguage

enum class CaiyunZHTargetLanguage(
    override val isoCode: String,
    override val languageName: String,
) : TargetLanguage {
    English("zh2en", "English"),
    Japanese("zh2ja", "Japanese");
}