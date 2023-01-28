package com.example.routing

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import dev.minimul.toolkit.routing.IRoute
import dev.minimul.toolkit.routing.Router
import dev.minimul.toolkit.routing.RouterBackHandler
import dev.minimul.toolkit.routing.RouterListener
import dev.minimul.toolkit.ui.compose.AutoSizeText
import dev.minimul.toolkit.ui.compose.between
import dev.minimul.toolkit.ui.compose.exactly
import org.koin.androidx.compose.get

class RoutingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }

}

@Composable
private fun Content() {
    val activity = LocalContext.current as Activity
    val router: MainRouter = get()

    @Suppress("RemoveExplicitTypeArguments", "LocalVariableName") var _history by remember {
        mutableStateOf<List<MainRoute>>(router.history)
    }

    DisposableEffect(Unit) {
        router.registerListener("MainScreen", object : RouterListener<MainRoute> {
            override fun onHistoryChanged(history: List<MainRoute>) {
                _history = history
            }

            override fun onBackFromDefaultRoute() {
                // nothing to do here
            }
        })

        onDispose {
            router.removeListener("MainScreen")
        }
    }

    val route = _history.lastOrNull() ?: return

    RouterBackHandler(router = router, onBackFromDefaultRoute = {
        /**
         * main router back from default route we close current activity.
         */
        activity.finish()
    })

    ScreenContainer(onGoBack = {
        router.back()
    }, onNavigate = { _route ->
        router.navigate(_route)
    }) {
        when (route) {
            MainRoute.FirstRoute -> FirstScreen()
            MainRoute.SecondRoute -> SecondScreen()
            MainRoute.ThirdRoute -> ThirdScreen()
        }
    }
}

sealed class MainRoute : IRoute {
    object FirstRoute : MainRoute()
    object SecondRoute : MainRoute()
    object ThirdRoute : MainRoute()
}

class MainRouter : Router<MainRoute>(MainRoute.FirstRoute)

@Composable
private fun ScreenContainer(
    onGoBack: () -> Unit, onNavigate: (route: MainRoute) -> Unit, content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onGoBack) {
                AutoSizeText(text = "go back", fontSizeRange = exactly(16.sp))
            }
            Button(onClick = { onNavigate(MainRoute.FirstRoute) }) {
                AutoSizeText(text = "first screen", fontSizeRange = exactly(16.sp))
            }
            Button(onClick = { onNavigate(MainRoute.SecondRoute) }) {
                AutoSizeText(text = "second screen", fontSizeRange = exactly(16.sp))
            }
            Button(onClick = { onNavigate(MainRoute.ThirdRoute) }) {
                AutoSizeText(text = "third screen", fontSizeRange = exactly(16.sp))
            }
        }
        content()
    }
}

@Composable
private fun FirstScreen() {
    AutoSizeText(text = "FirstScreen", fontSizeRange = between(16.sp, 20.sp))
}

@Composable
private fun SecondScreen() {
    AutoSizeText(text = "SecondScreen", fontSizeRange = between(16.sp, 20.sp))
}

@Composable
private fun ThirdScreen() {
    AutoSizeText(text = "ThirdScreen", fontSizeRange = between(16.sp, 20.sp))
}