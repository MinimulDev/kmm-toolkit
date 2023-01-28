package dev.minimul.toolkit.ui

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a border around layout for better look at dimensions.
 *
 * @param enabled   Whether border is enabled or not, defaults to `true`.
 *                  Useful if wrapped around another function that takes in your debug/release condition to not show in production.
 */
fun Modifier.devBorder(
    color: Color = Color.Red,
    size: Dp = 1.dp,
    enabled: Boolean = true
): Modifier {
    if (enabled) {
        return this.border(size, color)
    }
    return this
}