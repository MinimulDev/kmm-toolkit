package dev.minimul.toolkit.arch.lifecycle

import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

actual open class PlatformViewModel actual constructor() : ViewModel(), KoinComponent {
    actual open fun cleanup() {

    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }
}