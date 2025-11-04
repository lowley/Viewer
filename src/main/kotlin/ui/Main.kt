package lorry.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import lorry.basics.appModule
import lorry.logcat.ILogCatComponent
import lorry.logcat.LogCatComponent
import lorry.ui.utils.ExchangeModeChooser
import lorry.ui.utils.TerminalDisplay
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {
    startKoin {
        modules(appModule)
    }
    Window(onCloseRequest = ::exitApplication) {
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
            modifier = Modifier.fillMaxSize()
                .padding(top = 5.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                ExchangeModeChooser(
                    modifier = Modifier
                        .padding(start = 15.dp),
                    //après choix d'un exchangeMode
                    setExchangeMode = viewModel::setExchangeMode,
                    stopLogcatViewing = logcat::stopLogcatViewing
                )
            }

            TerminalDisplay(
                terminalContentFlow = viewModel.terminalContent
            )





        }
    }
}






