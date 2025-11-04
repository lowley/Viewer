package lorry.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import lorry.basics.appModule
import lorry.basics.getAll
import lorry.basics.getAllOf
import lorry.ui.utils.ExchangeMode
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
    val exchangeModes = getAllOf<ExchangeMode>()


    MaterialExpressiveTheme {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = 15.dp, top = 5.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                var selectedIndex by remember { mutableIntStateOf(0) }

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = "Connexion à")

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .height(35.dp),
                ) {
                    exchangeModes.sortedBy { it.index }.forEachIndexed { index, exchangeMode ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = exchangeModes.count()
                            ),
                            onClick = {
                                selectedIndex = index
                                viewModel.setExchangeMode(exchangeMode)
                            },
                            selected = index == selectedIndex,
                            label = { Text(exchangeMode.name) }
                        )
                    }


                }
            }
        }
    }
}



