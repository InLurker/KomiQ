package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.toSize
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.RotatingIcon
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReaderBackground
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextRecognition
import com.inlurker.komiq.ui.screens.helper.Enumerated.TranslationEngine

@Composable
fun <T> SettingsDropdown(
    label: String,
    options: List<T>,
    currentSelection: T,
    displayOption: (T) -> String, // Lambda function to convert option to String
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { dropdownExpanded = true }
            .onGloballyPositioned { layoutCoordinates ->
                rowSize = layoutCoordinates.size.toSize()
            }
    ) {

        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = displayOption(currentSelection),
            style = MaterialTheme.typography.labelLarge
        )
        // Assuming RotatingIcon is defined elsewhere in your code
        RotatingIcon(
            isRotated = dropdownExpanded,
            imageVector = Icons.Default.ExpandMore
        )


        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = dropdownModifier
                .width(with(LocalDensity.current) { rowSize.width.toDp() })
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = displayOption(option)) },
                    onClick = {
                        onOptionSelected(index)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }

}

@Composable
fun LanguageDropdownSettings(
    label: String,
    options: List<ComicLanguageSetting>,
    currentSelection: ComicLanguageSetting,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onLanguageSelected: (ComicLanguageSetting) -> Unit
) {
    SettingsDropdown(
        label = label,
        options = options,
        currentSelection = currentSelection,
        displayOption = { it.languageDisplayName() },
        modifier = modifier,
        dropdownModifier = dropdownModifier,
        onOptionSelected = { index -> onLanguageSelected(options[index]) }
    )
}

@Composable
fun ReadingDirectionDropdownSettings(
    label: String,
    options: List<ReadingDirection>,
    currentSelection: ReadingDirection,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onReadingDirectionSelected: (ReadingDirection) -> Unit
) {
    SettingsDropdown(
        label = label,
        options = options,
        currentSelection = currentSelection,
        displayOption = { it.description },
        modifier = modifier,
        dropdownModifier = dropdownModifier,
        onOptionSelected = { index -> onReadingDirectionSelected(options[index]) }
    )
}

@Composable
fun ReaderBackgroundDropdownSettings(
    label: String,
    options: List<ReaderBackground>,
    currentSelection: ReaderBackground,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onReaderBackgroundSelected: (ReaderBackground) -> Unit
) {
    SettingsDropdown(
        label = label,
        options = options,
        currentSelection = currentSelection,
        displayOption = { it.description },
        modifier = modifier,
        dropdownModifier = dropdownModifier,
        onOptionSelected = { index -> onReaderBackgroundSelected(options[index]) }
    )
}

@Composable
fun TextDetectionDropdownSettings(
    label: String,
    options: List<TextDetection>,
    currentSelection: TextDetection,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onTextDetectionSelected: (TextDetection) -> Unit
) {
    SettingsDropdown(
        label = label,
        options = options,
        currentSelection = currentSelection,
        displayOption = { it.description },
        modifier = modifier,
        dropdownModifier = dropdownModifier,
        onOptionSelected = { index -> onTextDetectionSelected(options[index]) }
    )
}

@Composable
fun TextRecognitionDropdownSettings(
    label: String,
    options: List<TextRecognition>,
    currentSelection: TextRecognition,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onTextRecognitionSelected: (TextRecognition) -> Unit
) {
    SettingsDropdown(
        label = label,
        options = options,
        currentSelection = currentSelection,
        displayOption = { it.description }, // Use the languageDisplayName function to display each language
        modifier = modifier,
        dropdownModifier = dropdownModifier,
        onOptionSelected = { index -> onTextRecognitionSelected(options[index]) }
    )
}

@Composable
fun TranslationEngineDropdownSettings(
    label: String,
    options: List<TranslationEngine>,
    currentSelection: TranslationEngine,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    onTranslationEngineSelected: (TranslationEngine) -> Unit
) {
    SettingsDropdown(
        label = label,
        options = options,
        currentSelection = currentSelection,
        displayOption = { it.description },
        modifier = modifier,
        dropdownModifier = dropdownModifier,
        onOptionSelected = { index -> onTranslationEngineSelected(options[index]) }
    )
}

