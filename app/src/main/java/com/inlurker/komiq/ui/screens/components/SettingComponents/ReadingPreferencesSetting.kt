package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.runtime.Composable

@Composable
fun ReadingPreferencesSetting(
    eyeCare: Int,
    onEyeCareChange: (Int) -> Unit,
    brightnessScale: Int,
    onBrightnessScaleChange: (Int) -> Unit,
    saturationScale: Int,
    onSaturationScaleChange: (Int) -> Unit
) {
    SettingSlider(
        icon = Icons.Outlined.RemoveRedEye,
        label = "Eye Care",
        value = eyeCare,
        onValueChange = onEyeCareChange
    )
    SettingSlider(
        icon = Icons.Outlined.Brightness4,
        label = "Brightness",
        value = brightnessScale,
        onValueChange = onBrightnessScaleChange
    )
    SettingSlider(
        icon = Icons.Outlined.Opacity,
        label = "Saturation",
        value = saturationScale,
        onValueChange = onSaturationScaleChange
    )
}
