package com.inlurker.komiq.ui.screens.helper.ReaderHelper

import com.inlurker.komiq.ui.screens.helper.Enumerated.ReaderBackground
import com.inlurker.komiq.ui.screens.helper.Enumerated.ReadingDirection

data class ReaderSettingsData(
    val readerBackground: ReaderBackground,
    val readingDirection: ReadingDirection,
    val eyeCareValue: Int,
    val brightnessValue: Int,
    val saturationValue: Int,
    val isGreyscaleEnabled: Boolean,
    val isInvertEnabled: Boolean
)