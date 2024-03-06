package com.inlurker.komiq.ui.screens.helper.Enumerated

enum class ReaderBackground(val description: String) {
    Default("Default"),
    Light("Light"),
    Dark("Dark");

    companion object {
        fun getOptionList(): List<ReaderBackground> {
            return listOf(Default, Light, Dark)
        }
    }
}
