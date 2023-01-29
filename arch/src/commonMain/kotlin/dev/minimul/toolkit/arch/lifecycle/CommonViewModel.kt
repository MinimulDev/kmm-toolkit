package dev.minimul.toolkit.arch.lifecycle

import dev.minimul.toolkit.arch.flow.wrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.inject

abstract class CommonViewModel : PlatformViewModel() {
    @Suppress("MemberVisibilityCanBePrivate")
    val dispatcher: PlatformDispatcher by inject()

    val scope = CoroutineScope(SupervisorJob() + dispatcher.main)

    fun <T : Any> Flow<T>.wrap() = wrap(scope)

    override fun cleanup() {
        super.cleanup()
        scope.coroutineContext.cancelChildren()
    }
}