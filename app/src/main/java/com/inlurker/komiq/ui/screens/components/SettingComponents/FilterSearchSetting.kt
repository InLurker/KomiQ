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
import org.koitharu.kotatsu.parsers.model.MangaTag


@Composable
fun FilterSearchSetting(
    currentComicLanguageSetting: ComicLanguageSetting,
    currentGenreFilter: List<GenreTag>,
    currentThemeFilter: List<ThemeTag>,
    currentKotatsuTagFilter: List<MangaTag>,
    kotatsuTagList: List<MangaTag>,
    onApplySettings: (ComicLanguageSetting, List<GenreTag>, List<ThemeTag>, List<MangaTag>) -> Unit
) {
    var tempComicLanguageSetting by remember { mutableStateOf(currentComicLanguageSetting) }
    var tempGenreFilter = currentGenreFilter
    var tempThemeFilter = currentThemeFilter
    var tempKotatsuTagFilter = currentKotatsuTagFilter

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

            if (tempComicLanguageSetting == ComicLanguageSetting.Japanese) {
                item {
                    tempKotatsuTagFilter = tagFilterSelection(
                        titleText = "Tags",
                        tagSelection = currentKotatsuTagFilter,
                        tagList = kotatsuTagList,
                        displayOption = { it.title }
                    )
                }
            } else {
                item {
                    tempGenreFilter = tagFilterSelection(
                        titleText = "Genre",
                        tagSelection = tempGenreFilter,
                        tagList = GenreTag.asList(),
                        displayOption = { it.description }
                    )
                }

                item {
                    tempThemeFilter = tagFilterSelection(
                        titleText = "Theme",
                        tagSelection = tempThemeFilter,
                        tagList = ThemeTag.asList(),
                        displayOption = { it.description }
                    )
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
                        tempThemeFilter,
                        tempKotatsuTagFilter
                    )
                }
            ) {
                Text("Apply")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> tagFilterSelection(
    titleText: String,
    tagSelection: List<T>,
    tagList: List<T>,
    displayOption: (T) -> String
): List<T> {
    var tempSelectionList = tagSelection

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = titleText,
            fontWeight = FontWeight.SemiBold
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tagList.forEach { tag ->
                var isButtonSelected by remember {
                    mutableStateOf(
                        tempSelectionList.contains(
                            tag
                        )
                    )
                }
                ToggleTags(
                    label = displayOption(tag),
                    isSelected = isButtonSelected,
                    onStateChange = { isSelected ->
                        isButtonSelected = isSelected
                        if (isSelected) {
                            if (!tempSelectionList.contains(tag))
                                tempSelectionList = tempSelectionList.plus(tag)
                        } else {
                            tempSelectionList = tempSelectionList.minus(tag)
                        }
                    }
                )
            }
        }
    }

    return tempSelectionList
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterSearchSetting() {
    FilterSearchSetting(
        currentComicLanguageSetting = ComicLanguageSetting.English,
        currentGenreFilter = listOf(),
        currentThemeFilter = listOf(),
        currentKotatsuTagFilter = listOf(),
        kotatsuTagList = listOf()
    ) { _, _, _, _ ->

    }
}