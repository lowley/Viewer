package lorry.logcat

interface ILogCatComponent {

    fun launchLogCatViewing(
        addTerminalLine: (String) -> Unit
    )

    fun stopLogcatViewing()

}