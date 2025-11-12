package lorry.ui

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lowley.version2.viewer.IViewerAppComponent
import io.github.lowley.version2.viewer.ViewerAppComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lorry.deviceAPI.IDeviceAPIComponent
import lorry.logcat.ILogCatComponent
import lorry.ui.utils.ExchangeMode
import lorry.ui.utils.ExchangeMode.*
import lorry.ui.utils.TerminalLine

class ViewerViewModel(
    private val logcat: ILogCatComponent,
    private val deviceAPI: IDeviceAPIComponent,
) : ViewModel() {

    typealias terminalContentMutableFlow = MutableStateFlow<List<TerminalLine>>
    typealias terminalContentFlow = StateFlow<List<TerminalLine>>

    val viewerAppComponent: IViewerAppComponent = ViewerAppComponent

    ////////////////
    // injections //
    ////////////////


    ///////////////////////////////////////////
    // mode d'échanges: type de canal écouté //
    ///////////////////////////////////////////

    private val _exchangeMode = MutableStateFlow<ExchangeMode>(None)
    val exchangeMode = _exchangeMode.asStateFlow()

    //modifié par [[ExchangeModeChooser]]
    fun setExchangeMode(mode: ExchangeMode) {
        _exchangeMode.value = mode
    }

    ////////////////////////
    // lignes du terminal //
    ////////////////////////

    private val _terminalContent: terminalContentMutableFlow = MutableStateFlow(listOf())
    val terminalContent: terminalContentFlow = _terminalContent.asStateFlow()

    fun setTerminalContent(content: List<TerminalLine>) {
        _terminalContent.update { content }
    }

    private val maxLines = 1000
    fun addTerminalLine(line: TerminalLine) {
        _terminalContent.update { curr ->
            val next = curr + line
            if (next.size > maxLines) next.takeLast(maxLines) else next
        }
    }

    init {
        viewModelScope.launch(Dispatchers.Default) { // travail lourd hors Main
            viewerAppComponent.logs
                .buffer(capacity = Channel.BUFFERED) // lisse les rafales, sans drop
                .catch { e -> e.printStackTrace() }  // ne laisse pas la collecte mourir
                .collect { event ->                  // ← pas collectLatest
                    val builder = AnnotatedString.Builder()

                    for (segment in event.richText.richSegments) {
                        val style = SpanStyle(
                            fontWeight = if (segment.style.bold) FontWeight.Bold else FontWeight.Normal,
                            textDecoration = if (segment.style.underline) TextDecoration.Underline else TextDecoration.None,
                        )
                        builder.withStyle(style) { append(segment.text.text) }
                    }

                    val line = TerminalLine(
                        text = builder.toAnnotatedString(),
                        color = Color.Red
                    )

                    withContext(Dispatchers.Main) {
                        addTerminalLine(line)
                    }
                }
        }
    }
}