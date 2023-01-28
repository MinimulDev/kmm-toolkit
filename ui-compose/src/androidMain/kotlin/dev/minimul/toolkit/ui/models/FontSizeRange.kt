package dev.minimul.toolkit.ui.models

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val DEFAULT_TEXT_STEP = 1.sp

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
)