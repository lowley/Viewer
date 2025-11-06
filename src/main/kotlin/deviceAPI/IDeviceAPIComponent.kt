package lorry.deviceAPI

import lorry.ui.utils.TerminalLine

interface IDeviceAPIComponent {

    fun launchDeviceAPIViewing(addTerminalLine: (TerminalLine) -> Unit)

    fun stopDeviceAPIViewing()

}