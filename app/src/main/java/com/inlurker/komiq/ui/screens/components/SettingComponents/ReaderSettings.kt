package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting
import com.inlurker.komiq.ui.screens.components.SegmentedButton
import com.inlurker.komiq.ui.screens.components.SegmentedButtonItem
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReaderBackground
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextDetection
import com.inlurker.komiq.ui.screens.helper.Enumerated.TextRecognition
import com.inlurker.komiq.ui.screens.helper.Enumerated.TranslationEngine
import com.inlurker.komiq.ui.screens.helper.ReaderHelper.AutomaticTranslationSettingsData
import com.inlurker.komiq.ui.screens.helper.ReaderHelper.ReaderSettingsData

@Composable
fun ReaderSettings(
    settingsData: ReaderSettingsData,
    onSettingsChanged: (ReaderSettingsData) -> Unit,
    autoTranslateSettingsData: AutomaticTranslationSettingsData,
    onAutoTranslateSettingsChanged: (AutomaticTranslationSettingsData) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        ReaderBackgroundDropdownSettings(
            label = "Background Colour",
            options = ReaderBackground.getOptionList(),
            currentSelection = settingsData.readerBackground,
            onReaderBackgroundSelected = { background ->
                onSettingsChanged(settingsData.copy(readerBackground = background))
            },
            modifier = Modifier
                .height(48.dp)
        )

        ReadingDirectionDropdownSettings(
            label = "Reading Layout",
            options = ReadingDirection.getOptionList(),
            currentSelection = settingsData.readingDirection,
            onReadingDirectionSelected = { direction ->
                onSettingsChanged(settingsData.copy(readingDirection = direction))
            },
            modifier = Modifier
                .height(48.dp)
        )

        Spacer(Modifier.height(8.dp))

        ReadingPreferencesSetting(
            eyeCare = settingsData.eyeCareValue,
            onEyeCareChange = { value ->
                onSettingsChanged(settingsData.copy(eyeCareValue = value))
            },
            brightnessScale = settingsData.brightnessValue,
            onBrightnessScaleChange = { value ->
                onSettingsChanged(settingsData.copy(brightnessValue = value))
            },
            saturationScale = settingsData.saturationValue,
            onSaturationScaleChange = { value ->
                onSettingsChanged(settingsData.copy(saturationValue = value))
            }
        )

        Spacer(Modifier.height(16.dp))

        SegmentedButton {
            SegmentedButtonItem(
                isSelected = settingsData.isGreyscaleEnabled,
                onClick = { isEnabled ->
                    onSettingsChanged(settingsData.copy(isGreyscaleEnabled = isEnabled))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Greyscale")
            }
            VerticalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(1.dp)
            )
            SegmentedButtonItem(
                isSelected = settingsData.isInvertEnabled,
                onClick = { isEnabled ->
                    onSettingsChanged(settingsData.copy(isInvertEnabled = isEnabled))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Invert")
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Automatic Translation",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Effortlessly enjoy comics in any languages as you read along.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        lineBreak = LineBreak.Simple
                    ),
                    fontWeight = FontWeight.Normal,
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 8.dp)
            )

            Switch(
                checked = autoTranslateSettingsData.enabled,
                onCheckedChange = { toggleResult ->
                    onAutoTranslateSettingsChanged(autoTranslateSettingsData.copy(enabled = toggleResult))
                },
                thumbContent = if (autoTranslateSettingsData.enabled) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }

        if(autoTranslateSettingsData.enabled) {

            Spacer(Modifier.height(8.dp))

            TextDetectionDropdownSettings(
                label = "Text Detection",
                options = TextDetection.getOptionList(),
                currentSelection = autoTranslateSettingsData.textDetection,
                onTextDetectionSelected = { textDetection ->
                    onAutoTranslateSettingsChanged(autoTranslateSettingsData.copy(textDetection = textDetection))
                },
                modifier = Modifier
                    .height(42.dp)
            )

            TextRecognitionDropdownSettings(
                label = "Text Recognition",
                options = TextRecognition.getOptionList(autoTranslateSettingsData.sourceLanguage),
                currentSelection = autoTranslateSettingsData.textRecognition,
                onTextRecognitionSelected = { textRecognition ->
                    onAutoTranslateSettingsChanged(autoTranslateSettingsData.copy(textRecognition = textRecognition))
                },
                modifier = Modifier
                    .height(42.dp)
            )

            TranslationEngineDropdownSettings(
                label = "Translation Engine",
                options = TranslationEngine.getOptionList(),
                currentSelection = autoTranslateSettingsData.translationEngine,
                onTranslationEngineSelected = { translationEngine ->
                    onAutoTranslateSettingsChanged(autoTranslateSettingsData.copy(translationEngine = translationEngine))
                },
                modifier = Modifier
                    .height(42.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReaderSettingsPreview() {
    var autoTranslateSettings by remember {
        mutableStateOf(
            AutomaticTranslationSettingsData(
                enabled = false,
                sourceLanguage = ComicLanguageSetting.Japanese,
                textDetection = TextDetection.CRAFT,
                textRecognition = TextRecognition.MangaOCR,
                translationEngine = TranslationEngine.Google
            )
        )
    }
    var settingsData by remember {
        mutableStateOf(
            ReaderSettingsData(
                readerBackground = ReaderBackground.Default,
                readingDirection = ReadingDirection.LeftToRight,
                eyeCareValue = 30,
                brightnessValue = 60,
                saturationValue = 90,
                isGreyscaleEnabled = false,
                isInvertEnabled = false
            )
        )
    }

    ReaderSettings(
        settingsData = settingsData,
        onSettingsChanged = { newSettingsData ->
            settingsData = newSettingsData
        },
        autoTranslateSettingsData = autoTranslateSettings,
        onAutoTranslateSettingsChanged = { newSettingsData ->
            autoTranslateSettings = newSettingsData
        }

    )
}