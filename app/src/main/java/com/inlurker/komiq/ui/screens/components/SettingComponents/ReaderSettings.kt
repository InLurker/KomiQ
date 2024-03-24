package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.ui.screens.components.SegmentedButton
import com.inlurker.komiq.ui.screens.components.SegmentedButtonItem
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReaderBackground
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection

@Composable
fun ReaderSettings(
    readerBackground: ReaderBackground,
    onReaderBackgroundSelected: (ReaderBackground) -> Unit,
    readingDirection: ReadingDirection,
    onReadingDirectionSelected: (ReadingDirection) -> Unit,
    eyeCareValue: Int,
    onEyeCareValueChange: (Int) -> Unit,
    brightnessValue: Int,
    onBrightnessValueChange: (Int) -> Unit,
    saturationValue: Int,
    onSaturationValueChange: (Int) -> Unit,
    isGreyscaleEnabled: Boolean,
    onGreyscaleChanged: (Boolean) -> Unit,
    isInvertEnabled: Boolean,
    onInvertChanged: (Boolean) -> Unit
) {
    Column {
        ReaderBackgroundDropdownSettings(
            label = "Background Colour",
            options = ReaderBackground.getOptionList(),
            currentSelection = readerBackground,
            onReaderBackgroundSelected = onReaderBackgroundSelected,
            modifier = Modifier
                .height(48.dp),
            dropdownModifier = Modifier
                .padding(horizontal = 32.dp)
        )

        ReadingDirectionDropdownSettings(
            label = "Reading Layout",
            options = ReadingDirection.getOptionList(),
            currentSelection = readingDirection,
            onReadingDirectionSelected = onReadingDirectionSelected,
            modifier = Modifier
                .height(48.dp)
        )

        Spacer(Modifier.height(16.dp))

        ReadingPreferencesSetting(
            eyeCare = eyeCareValue,
            onEyeCareChange = onEyeCareValueChange,
            brightnessScale = brightnessValue,
            onBrightnessScaleChange = onBrightnessValueChange,
            saturationScale = saturationValue,
            onSaturationScaleChange = onSaturationValueChange
        )

        Spacer(Modifier.height(8.dp))

        SegmentedButton {
            SegmentedButtonItem(
                isSelected = isGreyscaleEnabled,
                onClick = onGreyscaleChanged,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Greyscale")
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(1.dp)
            )
            SegmentedButtonItem(
                isSelected = isInvertEnabled,
                onClick = onInvertChanged,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Invert")
            }
        }
    }
}