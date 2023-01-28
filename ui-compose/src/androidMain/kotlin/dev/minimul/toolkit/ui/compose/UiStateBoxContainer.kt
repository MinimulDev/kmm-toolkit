package dev.minimul.toolkit.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.minimul.toolkit.arch.data.models.UiState

/**
 * Container for handling [UiState] conditional logic wrapped in [Box].
 *
 * @param modifier  Modifier for outer [Box].
 * @param state     Reference to UiState.
 * @param loading   Function for [UiState.Loading].
 * @param success   Function for [UiState.Success].
 * @param error     Function for [UiState.Error]
 */
@Composable
fun <S : Any, E : Any> UiStateContainer(
    modifier: Modifier = Modifier,
    state: UiState<S, E>,
    loading: @Composable BoxScope.() -> Unit = {},
    success: @Composable BoxScope.(value: S) -> Unit = {},
    error: @Composable BoxScope.(value: E) -> Unit = {},
) {
    Box(modifier = modifier) {
        when (state) {
            is UiState.Loading -> loading()
            is UiState.Success -> success(state.value)
            is UiState.Error -> error(state.value)
        }
    }
}