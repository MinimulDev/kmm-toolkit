package dev.minimul.toolkit.arch.domain

import dev.minimul.toolkit.arch.lifecycle.PlatformDispatcher
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("unused")
abstract class UseCase : KoinComponent {
    val dispatcher: PlatformDispatcher by inject()

    suspend inline fun <T> background(crossinline block: suspend CoroutineScope.() -> T): T =
        withContext(dispatcher.default) { block() }

    inline fun <T> CoroutineScope.backgroundAsync(crossinline block: suspend CoroutineScope.() -> T): Deferred<T> =
        async(dispatcher.default) { block() }
}