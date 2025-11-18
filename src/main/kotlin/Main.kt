package lorry

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.lowley.engineRoom.boat.SurfaceLogging
import io.github.lowley.engineRoom.common.StateMessage
import kotlinx.coroutines.flow.StateFlow
import lorry.deviceAPI.IDeviceAPIComponent
import lorry.logcat.ILogCatComponent
import lorry.ui.ViewerViewModel
import lorry.ui.utils.ExchangeModeChooser
import lorry.ui.utils.TerminalDisplay
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext
import lorry.basics.Viewer.appModule

fun main() = application {

    SurfaceLogging.startService()
    GlobalContext.startKoin {
        modules(appModule)
    }

    val deviceComponent: IDeviceAPIComponent = GlobalContext.get().get()
    SurfaceLogging.startService()

    Window(onCloseRequest = {
        deviceComponent.stopDeviceAPIViewing()
        exitApplication()
    }) {
        App()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun App() {

    val viewModel: ViewerViewModel = koinInject()
    val logcat: ILogCatComponent = koinInject()

    MaterialExpressiveTheme {
        Column(
            modifier = Modifier.Companion.fillMaxSize()
                .padding(top = 5.dp),
        ) {
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
            ) {
                ExchangeModeChooser(
                    modifier = Modifier.Companion
                        .padding(start = 15.dp),
                    //après choix d'un exchangeMode
                    setExchangeMode = viewModel::setExchangeMode,
                    stopLogcatViewing = logcat::stopLogcatViewing,
                    enableAndroidLogs = SurfaceLogging::toggleLogs
                )
            }

            TerminalDisplay(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                terminalContentFlow = viewModel.terminalContent
            )


            Row(Modifier.fillMaxWidth()
                .height(30.dp)) {
                StatusLine(
                    modifier = Modifier
                        .background(Color.LightGray),
                    messageFlow = SurfaceLogging.stateMessageFlow
                )
            }
        }
    }
}

context(scope: RowScope)
@Composable
fun StatusLine(
    modifier: Modifier,
    messageFlow: StateFlow<StateMessage>,

    ) {
    with(scope) {
        val message by messageFlow.collectAsState(initial = StateMessage.Companion.EMPTY)

        Text(
            modifier = modifier
                .padding(start = 10.dp, bottom = 5.dp)
                .fillMaxSize(),

            text = if (message == StateMessage.Companion.EMPTY) "" else message.message,
        )
    }
}