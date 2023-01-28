package dev.minimul.toolkit.arch

import dev.minimul.toolkit.arch.lifecycle.PlatformDispatcher
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val archCommonModule = module {
    singleOf(::PlatformDispatcher)
}