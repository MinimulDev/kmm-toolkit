package dev.minimul.toolkit.ui.compose

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.minimul.toolkit.ui.models.FontSizeRange

/**
 * Default choice for checking overflow state when drawing text.
 */
private fun defaultDidTextOverflow(layout: TextLayoutResult): Boolean {
    return layout.didOverflowHeight
}

@Composable
fun AutoSizeText(
    text: String,
    fontSizeRange: FontSizeRange,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    onLayoutCheckOverflow: (layout: TextLayoutResult) -> Boolean = ::defaultDidTextOverflow
) {
    AutoSizeText(
        AnnotatedString(text),
        fontSizeRange,
        modifier,
        color,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        style,
        onLayoutCheckOverflow
    )
}

@Composable
fun AutoSizeText(
    text: AnnotatedString,
    fontSizeRange: FontSizeRange,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
    onLayoutCheckOverflow: (layout: TextLayoutResult) -> Boolean = ::defaultDidTextOverflow
) {
    var fontSizeValue by remember { mutableStateOf(fontSizeRange.max.value) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        style = style,
        fontSize = fontSizeValue.sp,
        onTextLayout = layout@{
            if (readyToDraw) return@layout
            if (onLayoutCheckOverflow(it)) {
                val nextFontSizeValue = fontSizeValue - fontSizeRange.step.value
                if (nextFontSizeValue <= fontSizeRange.min.value) {
                    fontSizeValue = fontSizeRange.min.value
                    readyToDraw = true
                } else {
                    fontSizeValue = nextFontSizeValue
                }
            } else {
                readyToDraw = true
            }
        },
        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() }
    )
}

/**
 * @param min   Minimum font size.
 * @param max   Maximum font size.
 *
 * @return  [FontSizeRange] between [min] and [max] inclusive.
 */
fun between(min: TextUnit, max: TextUnit) = FontSizeRange(min = min, max = max)

/**
 * @param min   Minimum font size, converted to TextUnit.
 * @param max   Maximum font size, converted to TextUnit.
 *
 * @return  [FontSizeRange] between [min] and [max] inclusive.
 */
fun between(min: Int, max: Int) = FontSizeRange(min = min.sp, max = max.sp)

/**
 * @param min   Minimum font size, converted to TextUnit.
 * @param max   Maximum font size, converted to TextUnit.
 *
 * @return  [FontSizeRange] between [min] and [max] inclusive.
 */
fun between(min: Float, max: Float) = FontSizeRange(min = min.sp, max = max.sp)

/**
 * @param unit  Exact font size.
 *
 * @return  [FontSizeRange] with same font size set for both [FontSizeRange.min] and [FontSizeRange.max].
 */
fun exactly(unit: TextUnit) = FontSizeRange(min = unit, max = unit)

/**
 * @param unit  Exact font size, converted to TextUnit.
 *
 * @return  [FontSizeRange] with same font size set for both [FontSizeRange.min] and [FontSizeRange.max].
 */
fun exactly(unit: Int) = FontSizeRange(min = unit.sp, max = unit.sp)

/**
 * @param unit  Exact font size, converted to TextUnit.
 *
 * @return  [FontSizeRange] with same font size set for both [FontSizeRange.min] and [FontSizeRange.max].
 */
fun exactly(unit: Float) = FontSizeRange(min = unit.sp, max = unit.sp)
