package lorry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lorry.deviceAPI.IDeviceAPIComponent
import lorry.logcat.ILogCatComponent
import lorry.ui.utils.ExchangeMode
import lorry.ui.utils.ExchangeMode.*
import lorry.ui.utils.TerminalLine

class ViewerViewModel(
    private val logcat: ILogCatComponent,
    private val deviceAPI: IDeviceAPIComponent,
) : ViewModel() {

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

    typealias terminalContentMutableFlow = MutableStateFlow<List<TerminalLine>>
    typealias terminalContentFlow = StateFlow<List<TerminalLine>>

    private val _terminalContent: terminalContentMutableFlow = MutableStateFlow(listOf())
    val terminalContent: terminalContentFlow = _terminalContent.asStateFlow()

    fun setTerminalContent(content: List<TerminalLine>) {
        _terminalContent.update { content }
    }

    private val maxLines = 1000
    fun addTerminalLine(line: TerminalLine){
        _terminalContent.update { curr ->
            val next = curr + line
            if (next.size > maxLines) next.takeLast(maxLines) else next
        }
    }


    ////////////////////
    // initialisation //
    ////////////////////

    init {
        /////////////////////////////////////
        // pec changements de exchangeMode //
        /////////////////////////////////////

        viewModelScope.launch {
            exchangeMode.collect { mode: ExchangeMode ->
                when (mode) {
                    LogCat -> {
                        logcat.launchLogCatViewing(addTerminalLine = ::addTerminalLine)
                        deviceAPI.stopDeviceAPIViewing()
                    }

                    DeviceAPI -> {
                        logcat.stopLogcatViewing()
                        deviceAPI.launchDeviceAPIViewing(addTerminalLine = ::addTerminalLine)

                    }

                    Both -> {
                        logcat.launchLogCatViewing(addTerminalLine = ::addTerminalLine)
                        deviceAPI.launchDeviceAPIViewing(addTerminalLine = ::addTerminalLine)
                    }

                    else -> {
                        logcat.stopLogcatViewing()
                        deviceAPI.stopDeviceAPIViewing()
                    }
                }
            }
        }


    }

}