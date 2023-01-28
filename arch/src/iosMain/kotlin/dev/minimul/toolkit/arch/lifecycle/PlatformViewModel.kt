package dev.minimul.toolkit.arch.lifecycle

import org.koin.core.component.KoinComponent
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.native.internal.GC

actual open class PlatformViewModel actual constructor() : KoinComponent {
    actual open fun cleanup() {
        dispatch_async(dispatch_get_main_queue()) { GC.collect() }
    }
}