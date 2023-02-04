package dev.minimul.toolkit.arch.lifecycle

import co.touchlab.stately.collections.IsoMutableList
import dev.minimul.toolkit.arch.flow.CancellationListener
import dev.minimul.toolkit.arch.flow.FlowWrapper
import dev.minimul.toolkit.arch.flow.StateFlowWrapper
import dev.minimul.toolkit.arch.flow.wrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

abstract class CommonViewModel : PlatformViewModel() {
    private val cancellationListeners = IsoMutableList<CancellationListener>()

    @Suppress("MemberVisibilityCanBePrivate")
    val dispatcher: PlatformDispatcher by inject()

    val scope = CoroutineScope(SupervisorJob() + dispatcher.main)

    fun <T : Any> Flow<T>.wrap(): FlowWrapper<T> {
        val wrapper = wrap(scope)
        cancellationListeners.add(wrapper)
        return wrapper
    }
    fun <T : Any> StateFlow<T>.wrap(): StateFlowWrapper<T> {
        val wrapper = wrap(scope)
        cancellationListeners.add(wrapper)
        return wrapper
    }

    override fun cleanup() {
        super.cleanup()
        scope.coroutineContext.cancelChildren()
        cancellationListeners.access {
            it.forEach { listener ->
                listener.cancel()
            }
        }
        cancellationListeners.clear()
    }
}