package com.example

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.minimul.toolkit.arch.data.models.AtomicJob
import dev.minimul.toolkit.arch.data.models.UiState
import dev.minimul.toolkit.arch.lifecycle.ViewStateViewModel
import dev.minimul.toolkit.ui.compose.AutoSizeText
import dev.minimul.toolkit.ui.compose.UiStateContainer
import dev.minimul.toolkit.ui.devBorder
import dev.minimul.toolkit.ui.models.FontSizeRange
import kotlinx.coroutines.delay
import org.koin.androidx.compose.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

private typealias MainSuccess = UiState.Success<MainViewModel.Success, MainViewModel.Error>
private typealias MainError = UiState.Error<MainViewModel.Success, MainViewModel.Error>

class MainViewModel : ViewStateViewModel<MainViewModel.Success, MainViewModel.Error>() {

    class Success(val text: String)
    class Error(val error: String)

    private var initializeJob = AtomicJob()

    fun initialize() {
        initializeJob.clear()

        initializeJob.value = launch {
            delay(2000L)
            update { prev -> prev.copy(ui = MainSuccess(Success("success"))) }
            delay(2000L)
            update { prev -> prev.copy(ui = MainError(Error("error"))) }
        }
    }

    override fun cleanup() {
        super.cleanup()
        initializeJob.cleanup()
    }

}

@SuppressLint("ModifierFactoryExtensionFunction")
private fun BoxScope.textModifier() = Modifier
    .align(Alignment.Center)
    .devBorder()

@Composable
private fun Content() {
    val viewModel: MainViewModel = get()

    val viewState by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        viewModel.initialize()
        onDispose {
            viewModel.cleanup()
        }
    }

    val textFont = FontSizeRange(max = 30.sp, min = 12.sp)

    UiStateContainer(
        modifier = Modifier
            .fillMaxSize()
            .devBorder(),
        state = viewState.ui,
        loading = {
            AutoSizeText(text = "loading", fontSizeRange = textFont, modifier = textModifier())
        }, success = { value ->
            AutoSizeText(text = value.text, fontSizeRange = textFont, modifier = textModifier())
        }, error = { value ->
            AutoSizeText(text = value.error, fontSizeRange = textFont, modifier = textModifier())
        })
}