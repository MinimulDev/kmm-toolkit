package com.example.arch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.minimul.toolkit.arch.data.models.AtomicJob
import dev.minimul.toolkit.arch.data.models.Either
import dev.minimul.toolkit.arch.data.models.UiState
import dev.minimul.toolkit.arch.data.repositories.Repository
import dev.minimul.toolkit.arch.domain.UseCase
import dev.minimul.toolkit.arch.lifecycle.ViewStateViewModel
import dev.minimul.toolkit.arch.network.Api
import dev.minimul.toolkit.ui.compose.AutoSizeText
import dev.minimul.toolkit.ui.compose.UiStateContainer
import dev.minimul.toolkit.ui.compose.between
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.get
import org.koin.core.component.inject
import java.util.concurrent.atomic.AtomicInteger

class ArchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Content()
        }
    }
}

interface ArchRepository {
    suspend fun incrementAndGetCount(): Either<Int, Exception>
}

class ArchApi : Api() {
    // simulate network
    suspend fun loadCount() = execute { Either.Left(Unit) }
}

class ArchRepositoryImpl : Repository(), ArchRepository {

    private val cache = AtomicInteger()

    private val api: ArchApi by inject()

    override suspend fun incrementAndGetCount(): Either<Int, Exception> {
        return inMemoryResourceLoader(
            onInMemory = { cache.incrementAndGet() },
            onNetwork = {
                when (api.loadCount()) {
                    is Either.Left -> {
                        Either.Left(cache.get())
                    }
                    is Either.Right -> Either.Right(IllegalStateException("api error"))
                }
            },
            // simulate request network on even cache count
            shouldReloadInMemory = { it == null || it.rem(2) == 0 }
        )
    }

    override fun clearData() {
        cache.set(0)
    }
}

class GetCount : UseCase() {
    private val archRepository: ArchRepository by inject()

    suspend operator fun invoke() = background {
        return@background when (val res = archRepository.incrementAndGetCount()) {
            is Either.Left -> Result.Success(res.value)
            is Either.Right -> Result.Error
        }
    }

    sealed class Result {
        data class Success(val count: Int) : Result()
        object Error : Result()
    }

}

private typealias ArchSuccess = UiState.Success<ArchViewModel.Success, ArchViewModel.Error>
private typealias ArchError = UiState.Error<ArchViewModel.Success, ArchViewModel.Error>

class ArchViewModel : ViewStateViewModel<ArchViewModel.Success, ArchViewModel.Error>() {

    data class Success(
        val count: Int
    )

    object Error

    private val getCount: GetCount by inject()

    private val loadCountJob = AtomicJob()

    fun loadCount() {
        loadCountJob.clear()

        loadCountJob.value = launch {
            when (val res = getCount()) {
                is GetCount.Result.Success -> {
                    update { prev ->
                        prev.copy(ui = ArchSuccess(Success(res.count)))
                    }
                }
                is GetCount.Result.Error -> {
                    update { prev ->
                        prev.copy(ui = ArchError(Error))
                    }
                }
            }
        }
    }
}

@Composable
private fun Content() {
    val viewModel: ArchViewModel = get()
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadCount()
            delay(1_000)
        }
    }

    UiStateContainer(
        state = viewState.ui,
        success = { value ->
            AutoSizeText(text = "count: $value", fontSizeRange = between(12, 16))
        }
    )
}