package com.inlurker.komiq.ui.screens.helper.Enumerated


enum class ComicLanguageSetting(val isoCode: String, val nativeName: String, val flagEmoji: String) {
    Arabic("ar", "العربية", "\uD83C\uDDE6\uD83C\uDDEA"), // Saudi Arabia flag
    German("de", "Deutsch", "\uD83C\uDDE9\uD83C\uDDEA"), // Germany flag
    English("en", "English", "\uD83C\uDDEC\uD83C\uDDE7"), // United Kingdom flag
    Spanish("es", "Español", "\uD83C\uDDEA\uD83C\uDDF8"), // Spain flag
    LatinAmericanSpanish("es-la", "Español Latinoamericano", "\uD83C\uDDF2\uD83C\uDDFD"), // Mexico flag (represents Latin America)
    French("fr", "Français", "\uD83C\uDDEB\uD83C\uDDF7"), // France flag
    Hindi("hi", "हिन्दी", "\uD83C\uDDEE\uD83C\uDDF3"), // India flag
    Indonesian("id", "Bahasa Indonesia", "\uD83C\uDDEE\uD83C\uDDE9"), // Indonesia flag
    Italian("it", "Italiano", "\uD83C\uDDEE\uD83C\uDDF9"), // Italy flag
    Japanese("ja", "日本語", "\uD83C\uDDEF\uD83C\uDDF5"), // Japan flag
    Korean("ko", "한국어", "\uD83C\uDDF0\uD83C\uDDF7"), // South Korea flag
    Portuguese("pt", "Português", "\uD83C\uDDF5\uD83C\uDDF9"), // Portugal flag
    BrazilianPortuguese("pt-br", "Português Brasileiro", "\uD83C\uDDE7\uD83C\uDDF7"), // Brazil flag
    Russian("ru", "Русский", "\uD83C\uDDF7\uD83C\uDDFA"), // Russia flag
    Chinese("zh", "中文", "\uD83C\uDDE8\uD83C\uDDF3"), // China flag
    TraditionalChinese("zh-hk", "中文（繁体）", "\uD83C\uDDED\uD83C\uDDF0"); // Hong Kong flag

    companion object {
        fun getSortedByNativeName(): List<ComicLanguageSetting> {
            return values().sortedBy { it.nativeName }
        }
    }

    fun languageDisplayName(): String {
        return "$flagEmoji $nativeName"
    }
}