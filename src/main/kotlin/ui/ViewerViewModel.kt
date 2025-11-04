package lorry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lorry.logcat.ILogCatComponent
import lorry.ui.utils.ExchangeMode
import lorry.ui.utils.ExchangeMode.*

class ViewerViewModel(
    private val logcat: ILogCatComponent
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

    private val _terminalContent = MutableStateFlow<List<String>>(listOf<String>())
    val terminalContent = _terminalContent.asStateFlow()

    fun setTerminalContent(content: List<String>) {
        _terminalContent.update { content }
    }

    fun addTerminalLine(line: String){
        _terminalContent.update { it + line }
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
                        logcat.launchLogCatViewing(
                            addTerminalLine = ::addTerminalLine
                        )
                    }

                    DeviceAPI -> {}
                    Both -> {}
                    else -> {}
                }
            }
        }


    }

}