package dev.minimul.toolkit.arch.data.models

sealed class UiState<out S : Any, out E : Any> {
    @Suppress("CanSealedSubClassBeObject")
    class Loading : UiState<Nothing, Nothing>()
    class Success<S : Any, E : Any>(val value: S) : UiState<S, E>()
    class Error<S : Any, E : Any>(val value: E) : UiState<S, E>()
}