package dev.minimul.toolkit.arch.network

import dev.minimul.toolkit.arch.data.models.Either
import org.koin.core.component.KoinComponent
import dev.minimul.toolkit.arch.lifecycle.PlatformDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.inject

@Suppress("unused")
abstract class Api : KoinComponent {

    private val dispatcher: PlatformDispatcher by inject()

    suspend fun <T> execute(block: suspend () -> T): Either<T, Exception> = withContext(dispatcher.io) {
        return@withContext try {
            Either.Left(block())
        } catch (e: Exception) {
            Either.Right(e)
        }
    }

}