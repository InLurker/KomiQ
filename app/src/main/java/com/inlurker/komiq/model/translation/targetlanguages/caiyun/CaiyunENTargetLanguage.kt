package com.inlurker.komiq.model.translation.targetlanguages.caiyun

import com.inlurker.komiq.model.translation.targetlanguages.TargetLanguage

enum class CaiyunENTargetLanguage(
    override val isoCode: String,
    override val languageName: String,
) : TargetLanguage {
    Chinese("en2zh", "Chinese");
}