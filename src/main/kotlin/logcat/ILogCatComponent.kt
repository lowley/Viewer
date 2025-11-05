package lorry.logcat

import lorry.ui.utils.TerminalLine

interface ILogCatComponent {

    fun launchLogCatViewing(
        addTerminalLine: (TerminalLine) -> Unit
    )

    fun stopLogcatViewing()

}