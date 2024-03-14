package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.model.data.mangadexapi.constants.GenreTag
import com.inlurker.komiq.model.data.mangadexapi.constants.ThemeTag
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.ToggleTags


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSearchSetting(
    currentComicLanguageSetting: ComicLanguageSetting,
    currentGenreFilter: List<GenreTag>,
    currentThemeFilter: List<ThemeTag>,
    onApplySettings: (ComicLanguageSetting, List<GenreTag>, List<ThemeTag>) -> Unit
) {
    var tempComicLanguageSetting by remember { mutableStateOf(currentComicLanguageSetting) }
    var tempGenreFilter = currentGenreFilter
    var tempThemeFilter = currentThemeFilter
    Column {
        LazyColumn(
            userScrollEnabled = true,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp)
                .height(260.dp)
        ) {
            item {
                val languageOptions = ComicLanguageSetting.asList()
                LanguageDropdownSettings(
                    label = "Language",
                    options = languageOptions,
                    currentSelection = tempComicLanguageSetting,
                    onLanguageSelected = { selectedLanguage ->
                        tempComicLanguageSetting = selectedLanguage
                    },
                    modifier = Modifier.height(48.dp),
                    dropdownModifier = Modifier
                        .heightIn(max = 180.dp)
                )
            }

            item {
                Text(
                    text = "Genre",
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GenreTag.asList().forEach { genre ->
                        var isButtonSelected by  remember { mutableStateOf(tempGenreFilter.contains(genre)) }
                        ToggleTags(
                            label = genre.description,
                            isSelected = isButtonSelected,
                            onStateChange = { isSelected ->
                                isButtonSelected = isSelected
                                if (isSelected) {
                                    if (!tempGenreFilter.contains(genre)) tempGenreFilter = tempGenreFilter.plus(genre)
                                } else {
                                    tempGenreFilter = tempGenreFilter.minus(genre)
                                }
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Theme",
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ThemeTag.asList().forEach { theme ->
                        var isButtonSelected by remember { mutableStateOf(tempThemeFilter.contains(theme)) }
                        ToggleTags(
                            label = theme.description,
                            isSelected = isButtonSelected,
                            onStateChange = { isSelected ->
                                isButtonSelected = isSelected
                                if (isSelected) {
                                    if (!tempThemeFilter.contains(theme)) tempThemeFilter = tempThemeFilter.plus(theme)
                                } else {
                                    tempThemeFilter = tempThemeFilter.minus(theme)
                                }
                            }
                        )
                    }
                }
            }
        }
        Row {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    onApplySettings(
                        tempComicLanguageSetting,
                        tempGenreFilter,
                        tempThemeFilter
                    )
                }
            ) {
                Text("Apply")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFilterSearchSetting() {
    FilterSearchSetting(
        currentComicLanguageSetting = ComicLanguageSetting.English,
        currentGenreFilter = listOf(),
        currentThemeFilter = listOf(),
        onApplySettings = { _, _, _ ->

        }
    )
}