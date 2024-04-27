package com.inlurker.komiq.model.data.repository


enum class ComicLanguageSetting(val isoCode: String, val nativeName: String, val flagEmoji: String) {
    Indonesian("id", "Bahasa Indonesia", "\uD83C\uDDEE\uD83C\uDDE9"), // Indonesia flag
    German("de", "Deutsch", "\uD83C\uDDE9\uD83C\uDDEA"), // Germany flag
    English("en", "English", "\uD83C\uDDEC\uD83C\uDDE7"), // United Kingdom flag
    Spanish("es", "Español", "\uD83C\uDDEA\uD83C\uDDF8"), // Spain flag
    LatinAmericanSpanish("es-la", "Español Latinoamericano", "\uD83C\uDDF2\uD83C\uDDFD"), // Mexico flag (represents Latin America)
    French("fr", "Français", "\uD83C\uDDEB\uD83C\uDDF7"), // France flag
    Italian("it", "Italiano", "\uD83C\uDDEE\uD83C\uDDF9"), // Italy flag
    Portuguese("pt", "Português", "\uD83C\uDDF5\uD83C\uDDF9"), // Portugal flag
    BrazilianPortuguese("pt-br", "Português Brasileiro", "\uD83C\uDDE7\uD83C\uDDF7"), // Brazil flag
    Russian("ru", "Русский", "\uD83C\uDDF7\uD83C\uDDFA"), // Russia flag
    Arabic("ar", "العربية", "\uD83C\uDDF8\uD83C\uDDE6"), // Saudi Arabia flag
    Hindi("hi", "हिन्दी", "\uD83C\uDDEE\uD83C\uDDF3"), // India flag
    Chinese("zh", "中文", "\uD83C\uDDE8\uD83C\uDDF3"), // China flag
    TraditionalChinese("zh-hk", "中文（繁体）", "\uD83C\uDDED\uD83C\uDDF0"), // Hong Kong flag
    Japanese("ja", "日本語", "\uD83C\uDDEF\uD83C\uDDF5"), // Japan flag
    Korean("ko", "한국어", "\uD83C\uDDF0\uD83C\uDDF7"); // South Korea flag

    companion object {
        fun asList(): List<ComicLanguageSetting> {
            return values().asList()
        }

        fun fromIsoCode(isoCode: String): ComicLanguageSetting? {
            return values().find { it.isoCode == isoCode }
        }

    }

    fun languageDisplayName(): String {
        return "$flagEmoji $nativeName"
    }

    fun toDeepLIsoCode():String? {
        return when (this) {
            Indonesian -> "ID" // Indonesian
            German -> "DE" // German
            English -> "EN" // English
            Spanish, LatinAmericanSpanish -> "ES" // Spanish
            French -> "FR" // French
            Italian -> "IT" // Italian
            Portuguese, BrazilianPortuguese -> "PT" // Portuguese
            Russian -> "RU" // Russian
            Arabic -> "AR" // Arabic
            Chinese, TraditionalChinese -> "ZH" // Chinese
            Japanese -> "JA" // Japanese
            Korean -> "KO" // Korean
            else -> null // Default case for unsupported or undefined languages
        }
    }

    fun toPaddleOCRType():String? {
        return when (this) {
            Arabic -> "ar"
            English -> "en"
            Chinese, TraditionalChinese -> "ch"
            French -> "fr"
            German -> "german"
            Japanese -> "japan"
            Korean -> "korean"
            else -> null
        }
    }
}