package lorry.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import lorry.ui.utils.ExchangeMode

class ViewerViewModel {

    ///////////////////////////////////////////
    // mode d'échanges: type de canal écouté //
    ///////////////////////////////////////////

    private val _exchangeMode = MutableStateFlow<ExchangeMode>(ExchangeMode.None)
    val exchangeMode = _exchangeMode.asStateFlow()

    fun setExchangeMode(mode: ExchangeMode) {
        _exchangeMode.value = mode
    }


}