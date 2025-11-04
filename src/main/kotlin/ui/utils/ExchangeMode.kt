package lorry.ui.utils

sealed class ExchangeMode(val name: String, val index: Int) {

    data object LogCat : ExchangeMode("Logcat", 2)

    data object DeviceAPI : ExchangeMode("Application", 3)

    data object Both : ExchangeMode("Les 2", 4)

    data object None: ExchangeMode("Aucun", 1)


}