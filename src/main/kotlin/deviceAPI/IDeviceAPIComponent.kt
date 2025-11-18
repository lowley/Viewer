package lorry.deviceAPI

import lorry.ui.utils.TerminalLine

interface IDeviceAPIComponent {

    suspend fun launchDeviceAPIViewing(addTerminalLine: (TerminalLine) -> Unit)

    fun stopDeviceAPIViewing()

}