package dev.minimul.toolkit.arch.data.models

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

/**
 * Utility class for handling [Job] interactions atomically.
 */
class AtomicJob {
    private val _job = AtomicReference<Job?>(null)

    var value
        get() = _job.value
        set(value) {
            _job.value = value
        }

    /**
     * Cancels running instances of job.
     */
    fun clear() {
        _job.value?.cancelChildren()
    }

    /**
     * Cancels running instances of job and cleans up reference.
     */
    fun cleanup() {
        clear()
        _job.value = null
    }
}