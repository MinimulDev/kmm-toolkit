package dev.minimul.toolkit.arch.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

fun <T : Any> Flow<T>.wrap(scope: CoroutineScope) = FlowWrapper(scope, this)

class FlowWrapper<T : Any>(
    private val scope: CoroutineScope,
    private val flow: Flow<T>
) {
    fun subscribe(
        onEvent: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ): Job =
        flow
            .onEach { onEvent(it) }
            .catch { onError(it) }
            .onCompletion { onComplete() }
            .launchIn(scope)
}