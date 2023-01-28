package dev.minimul.toolkit.arch.data.repositories

import dev.minimul.toolkit.arch.data.models.Either
import org.koin.core.component.KoinComponent

private fun <T> defaultShouldReloadInMemory(t: T?): Boolean {
    return t == null
}

abstract class Repository : KoinComponent {

    abstract fun clearData()

    suspend fun <T> inMemoryResourceLoader(
        onInMemory: () -> T?,
        onNetwork: suspend () -> Either<T, Exception>,
        shouldReloadInMemory: (t: T?) -> Boolean = ::defaultShouldReloadInMemory,
        refresh: Boolean = false
    ): Either<T, Exception> {
        if (refresh) return onNetwork()
        val inMemory = onInMemory()
        if (inMemory != null && !shouldReloadInMemory(inMemory)) return Either.Left(inMemory)
        return onNetwork()
    }
}