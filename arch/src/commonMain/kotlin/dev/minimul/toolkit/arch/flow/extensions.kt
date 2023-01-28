package dev.minimul.toolkit.arch.flow

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.compareAndUpdate(block: (prev: T) -> T): T {
    val prev = value
    val updated = block(prev)
    if (updated != prev) {
        value = updated
        return updated
    }
    return prev
}