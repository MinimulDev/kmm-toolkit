package dev.minimul.toolkit.arch.lifecycle

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class PlatformDispatcher actual constructor() {
    actual val main: CoroutineDispatcher = Dispatchers.Main
    actual val io: CoroutineDispatcher = Dispatchers.IO
    actual val default: CoroutineDispatcher = Dispatchers.Default
}