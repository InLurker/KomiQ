package com.inlurker.komiq.model.translation.targetlanguages

enum class DeepLTargetLanguage(
    override val isoCode: String,
    override val languageName: String,
) : TargetLanguage {
    Arabic("AR", "Arabic"),
    Bulgarian("BG", "Bulgarian"),
    Chinese("ZH", "Chinese (Simplified)"),
    Czech("CS", "Czech"),
    Danish("DA", "Danish"),
    German("DE", "German"),
    Greek("EL", "Greek"),
    English("EN", "English"),
    Spanish("ES", "Spanish"),
    Estonian("ET", "Estonian"),
    Finnish("FI", "Finnish"),
    French("FR", "French"),
    Hungarian("HU", "Hungarian"),
    Indonesian("ID", "Indonesian"),
    Italian("IT", "Italian"),
    Japanese("JA", "Japanese"),
    Korean("KO", "Korean"),
    Lithuanian("LT", "Lithuanian"),
    Latvian("LV", "Latvian"),
    Norwegian("NB", "Norwegian"),
    Dutch("NL", "Dutch"),
    Polish("PL", "Polish"),
    Portuguese_BR("PT", "Portuguese"),
    Romanian("RO", "Romanian"),
    Russian("RU", "Russian"),
    Slovak("SK", "Slovak"),
    Slovenian("SL", "Slovenian"),
    Swedish("SV", "Swedish"),
    Turkish("TR", "Turkish"),
    Ukrainian("UK", "Ukrainian");

    companion object {
        fun asList(): List<DeepLTargetLanguage> {
            return DeepLTargetLanguage.values().asList()
        }
    }
}