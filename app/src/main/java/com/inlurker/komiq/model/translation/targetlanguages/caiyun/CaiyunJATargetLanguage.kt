package com.inlurker.komiq.model.translation.targetlanguages.caiyun

import com.inlurker.komiq.model.translation.targetlanguages.TargetLanguage

enum class CaiyunJATargetLanguage(
    override val isoCode: String,
    override val languageName: String,
) : TargetLanguage {
    Chinese("ja2zh", "Chinese");
}