package dev.minimul.toolkit.arch.lifecycle

import org.koin.core.component.KoinComponent

expect open class PlatformViewModel constructor() : KoinComponent {
    open fun cleanup()
}