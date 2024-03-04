package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.inlurker.komiq.ui.screens.helper.LanguageHelper.ComicLanguageSetting

@Composable
fun LanguageSettingDropdownMenu(
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onLanguageSelected: (ComicLanguageSetting) -> Unit
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onDismissRequest
    ) {
        ComicLanguageSetting.values().forEach { language ->
            DropdownMenuItem(
                text = {
                    LanguageDropdownItem(languageName = language.nativeName, flagEmoji = getFlagEmoji(language))
                },
                onClick = {
                    onLanguageSelected
                }
            )
        }
    }
}

@Composable
fun LanguageDropdownItem(languageName: String, flagEmoji: String) {
    Text(text = flagEmoji + languageName)
}

fun getFlagEmoji(language: ComicLanguageSetting): String {
    return when (language) {
        ComicLanguageSetting.Arabic -> "\uD83C\uDDE6\uD83C\uDDEA" // Saudi Arabia flag
        ComicLanguageSetting.BrazilianPortuguese -> "\uD83C\uDDE7\uD83C\uDDF7" // Brazil flag
        ComicLanguageSetting.CastilianSpanish -> "\uD83C\uDDEA\uD83C\uDDF8" // Spain flag
        ComicLanguageSetting.Chinese -> "\uD83C\uDDE8\uD83C\uDDF3" // China flag
        ComicLanguageSetting.English -> "\uD83C\uDDEC\uD83C\uDDE7" // United Kingdom flag
        ComicLanguageSetting.French -> "\uD83C\uDDEB\uD83C\uDDF7" // France flag
        ComicLanguageSetting.German -> "\uD83C\uDDE9\uD83C\uDDEA" // Germany flag
        ComicLanguageSetting.Hindi -> "\uD83C\uDDEE\uD83C\uDDF3" // India flag
        ComicLanguageSetting.Indonesian -> "\uD83C\uDDEE\uD83C\uDDE9" // Indonesia flag
        ComicLanguageSetting.Italian -> "\uD83C\uDDEE\uD83C\uDDF9" // Italy flag
        ComicLanguageSetting.Japanese -> "\uD83C\uDDEF\uD83C\uDDF5" // Japan flag
        ComicLanguageSetting.Korean -> "\uD83C\uDDF0\uD83C\uDDF7" // South Korea flag
        ComicLanguageSetting.LatinAmericanSpanish -> "\uD83C\uDDF5\uD83C\uDDF7" // Mexico flag (represents Latin America)
        ComicLanguageSetting.Portuguese -> "\uD83C\uDDF5\uD83C\uDDF9" // Portugal flag
        ComicLanguageSetting.Russian -> "\uD83C\uDDF7\uD83C\uDDFA" // Russia flag
        ComicLanguageSetting.Spanish -> "\uD83C\uDDEA\uD83C\uDDF8" // Spain flag
        ComicLanguageSetting.TraditionalChinese -> "\uD83C\uDDED\uD83C\uDDF0" // Hong Kong flag
    }
}