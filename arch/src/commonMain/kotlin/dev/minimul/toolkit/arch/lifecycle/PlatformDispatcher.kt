package dev.minimul.toolkit.arch.lifecycle

import kotlinx.coroutines.CoroutineDispatcher

expect class PlatformDispatcher constructor() {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}