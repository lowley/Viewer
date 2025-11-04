package lorry.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lorry.ui.ViewerViewModel

context(scope: ColumnScope)
@Composable
fun TerminalDisplay(
    terminalContentFlow: ViewerViewModel.terminalContentFlow
) = with(scope) {

    val terminalContent by terminalContentFlow.collectAsState()
    val listState = rememberLazyListState()

    // À chaque changement de taille de la liste, on va tout en bas
    LaunchedEffect(terminalContent.size) {
        if (terminalContent.isNotEmpty()) {
            listState.scrollToItem(terminalContent.lastIndex)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(color = Color(0xFFDADADA), shape = RoundedCornerShape(8.dp)),
        state = listState
    ) {
        items(terminalContent) { line ->
            Text(text = line,)
        }
    }
}
