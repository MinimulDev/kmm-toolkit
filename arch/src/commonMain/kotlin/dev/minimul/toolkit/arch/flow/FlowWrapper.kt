package dev.minimul.toolkit.arch.flow

import co.touchlab.stately.collections.IsoMutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

fun <T : Any> Flow<T>.wrap(scope: CoroutineScope) = FlowWrapper(this, scope)

class FlowWrapper<T : Any>(
    private val origin: Flow<T>,
    private val scope: CoroutineScope,
) : Flow<T> by origin, CancellationListener {
    private val jobs = IsoMutableList<Job>()

    fun subscribe(
        onEvent: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ): Job {
        val job = origin
            .onEach { onEvent(it) }
            .catch { onError(it) }
            .onCompletion { onComplete() }
            .launchIn(scope)
        jobs.add(job)
        return job
    }

    override fun cancel() {
        jobs.access {
            it.forEach { job ->
                job.cancel()
            }
        }
        jobs.clear()
    }

}