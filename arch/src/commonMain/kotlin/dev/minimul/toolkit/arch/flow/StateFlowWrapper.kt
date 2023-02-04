package dev.minimul.toolkit.arch.flow

import co.touchlab.stately.collections.IsoMutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.*

fun <T : Any> StateFlow<T>.wrap(scope: CoroutineScope) = StateFlowWrapper(this, scope)

class StateFlowWrapper<T : Any>(
    private val origin: StateFlow<T>,
    private val scope: CoroutineScope,
) : StateFlow<T> by origin, CancellationListener {

    private val jobs = IsoMutableList<Job>()

    fun subscribe(
        onEvent: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Job {
        val job = origin
            .onEach { onEvent(it) }
            .catch { onError(it) }
            .launchIn(scope)
        jobs.add(job)
        return job
    }

    override fun cancel() {
        jobs.access {
            it.forEach { sub ->
                sub.cancel()
            }
        }
        jobs.clear()
    }
}