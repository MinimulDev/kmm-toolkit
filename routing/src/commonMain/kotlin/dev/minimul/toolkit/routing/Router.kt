package dev.minimul.toolkit.routing

import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.collections.IsoMutableMap
import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import kotlin.jvm.JvmField
import kotlin.reflect.typeOf

data class NavigateOptions(
    val replace: Boolean = false, val clearStack: Boolean = false
) {
    companion object {
        @JvmField
        @Suppress("unused")
        val clearHistory = NavigateOptions(clearStack = true)
    }
}

interface IRouter<T: IRoute> {
    fun navigate(route: T, options: NavigateOptions = NavigateOptions(), emit: Boolean = true)
    fun back(emit: Boolean = true): Boolean
}

interface RouterListener<T : IRoute> {
    fun onHistoryChanged(history: List<T>)
    fun onBackFromDefaultRoute()
}

abstract class Router<T : IRoute>(
    private val defaultRoute: T
) : IRouter<T> {

    private val _history = IsoMutableList<T>()
    val history get() = _history.access { it.toList() }

    private val _listeners = IsoMutableMap<String, RouterListener<T>>()

    init {
        _history.access { it.add(defaultRoute) }
    }

    fun registerListener(key: String, listener: RouterListener<T>) {
        _listeners[key] = listener
    }

    fun removeListener(key: String) {
        _listeners.remove(key)
    }

    override fun navigate(route: T, options: NavigateOptions, emit: Boolean) {
        _history.access { history ->
            val ktl = history.toMutableList()
            val prev = ktl.lastOrNull()
            if (route == prev) return@access
            if (options.clearStack) {
                ktl.clear()
                ktl.add(defaultRoute) // will always have default route
            }
            val last = history.size
            if (last > 0 && options.replace) {
                ktl[last] = route
            } else {
                ktl.add(route)
            }
            history.clear()
            history.addAll(ktl)
            if (emit) onHistoryChanged(ktl)
        }
    }

    override fun back(emit: Boolean): Boolean {
        return _history.access { history ->
            val ktl = history.toMutableList()
            if (ktl.isEmpty()) return@access false
            val defaultRouteReached = hasReachedDefaultRoute(ktl)
            if (defaultRouteReached) {
                backOnDefaultRouteReached()
                return@access false
            }
            ktl.removeLast()
            history.clear()
            history.addAll(ktl)
            if (emit) onHistoryChanged(ktl)
            true
        }
    }

    private fun hasReachedDefaultRoute(history: List<T>): Boolean {
        return history.size == 1
    }

    private fun onHistoryChanged(history: List<T>) {
        _listeners.access { listeners ->
            listeners.forEach { (_, listener) ->
                listener.onHistoryChanged(history)
            }
        }
    }

    private fun backOnDefaultRouteReached() {
        _listeners.access { listeners ->
            listeners.forEach { (_, listener) ->
                listener.onBackFromDefaultRoute()
            }
        }
    }

}