package com.inlurker.komiq.ui.screens.helper.Enumerated

enum class ReadingDirection(val description: String) {
    LeftToRight("Left to Right"),
    RightToLeft("Right to Left"),
    TopToBottom("Top to Bottom"),
    BottomToTop("Bottom to Top");

    companion object {
        fun getOptionList(): List<ReadingDirection> {
            return listOf(LeftToRight, RightToLeft, TopToBottom, BottomToTop)
        }
    }
}
