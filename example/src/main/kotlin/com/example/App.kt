package com.example

import android.app.Application
import dev.minimul.toolkit.arch.archCommonModule
import com.example.arch.ArchRepository
import com.example.arch.ArchRepositoryImpl
import com.example.arch.ArchViewModel
import com.example.arch.ArchApi
import com.example.arch.GetCount
import com.example.arch.ResetCount
import com.example.routing.MainRouter
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

class App : Application() {

    private val mainModule = module {
        singleOf(::MainViewModel)
    }

    private val routingModule = module {
        singleOf(::MainRouter)
    }

    private val archModule = module {
        singleOf(::ArchApi)
        singleOf(::ArchViewModel)
        singleOf(::ArchRepositoryImpl) bind ArchRepository::class
        singleOf(::GetCount)
        singleOf(::ResetCount)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                listOf(
                    archCommonModule,
                    mainModule,
                    archModule,
                    routingModule
                )
            )
        }
    }

}