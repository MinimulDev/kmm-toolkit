package dev.minimul.toolkit.arch.lifecycle

import dev.minimul.toolkit.arch.data.models.UiState

open class ViewStateViewModel<S : Any, E : Any>
    : CommonStateViewModel<ViewStateViewModel.ViewState<S, E>>(ViewState()) {

    data class ViewState<S : Any, E : Any>(val ui: UiState<S, E> = UiState.Loading())

}