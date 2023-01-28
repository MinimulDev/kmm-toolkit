package dev.minimul.toolkit.routing

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

private const val ROUTER_LISTENER_KEY = "dev.minimul.toolkit.routing.RouterBackHandler"

/**
 *  Back handler attached to [Router] state. Similar to [BackHandler], you must either, extend your activity
 *  with [androidx.activity.ComponentActivity], or provide your own [OnBackPressedDispatcherOwner]; otherwise this handler will do nothing.
 *
 *  @param router   The router to listen for back state changes.
 *  @param onBackFromDefaultRoute   Function to handle when we are attempting to pop from [Router.defaultRoute].
 *  [onBackFromDefaultRoute] is invoked from main thread.
 *  Possible use is in single router architecture we can call [Activity.finish] to close current activity window.
 *
 */
@Composable
fun <T : IRoute> RouterBackHandler(
    router: Router<T>,
    onBackFromDefaultRoute: () -> Unit
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher ?: return
    val lifecycle = LocalLifecycleOwner.current

    val cb = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                router.back()
            }
        }
    }

    val listener = remember {
        object : RouterListener<T> {
            override fun onHistoryChanged(history: List<T>) {
                // nothing to do here
            }

            override fun onBackFromDefaultRoute() {
                lifecycle.lifecycleScope.launch { onBackFromDefaultRoute() }
            }

        }
    }
    DisposableEffect(Unit) {
        router.registerListener(ROUTER_LISTENER_KEY, listener)
        onDispose {
            router.removeListener(ROUTER_LISTENER_KEY)
        }
    }
    DisposableEffect(lifecycle, dispatcher) {
        dispatcher.addCallback(lifecycle, cb)
        onDispose {
            cb.remove()
        }
    }
}