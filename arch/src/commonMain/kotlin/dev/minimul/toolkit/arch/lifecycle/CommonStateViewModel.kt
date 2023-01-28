package dev.minimul.toolkit.arch.lifecycle

import dev.minimul.toolkit.arch.flow.compareAndUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class CommonStateViewModel<T : Any>(default: T) : CommonViewModel() {

    private val _state = MutableStateFlow(default)
    val state = _state.wrap()

    fun update(block: (prev: T) -> T): T {
        return _state.compareAndUpdate { prev -> block(prev) }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch { block() }
    }
}