package lorry.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import lorry.basics.appModule
import lorry.basics.getAll
import lorry.basics.getAllOf
import lorry.ui.utils.ExchangeMode
import lorry.ui.utils.ExchangeModeChooser
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
    val terminalContent by viewModel.terminalContent.collectAsState()

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
                )
            }

            val listState = rememberLazyListState()
            // À chaque changement de taille de la liste, on va tout en bas
            LaunchedEffect(terminalContent.size) {
                if (terminalContent.isNotEmpty()) {
                    listState.scrollToItem(terminalContent.lastIndex)
                    // ou animateScrollToItem(...) si tu veux un défilement animé
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .background(color = Color(0xFFDADADA), shape = RoundedCornerShape(8.dp)),
                state = listState
            ) {
                items(terminalContent){ line ->
                    Text(
                        text = line,
                    )
                }
            }





        }
    }
}






