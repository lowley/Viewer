package lorry.logcat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LogCatComponent: ILogCatComponent {

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun launchLogCatViewing(addTerminalLine: (String) -> Unit) {

        scope.launch {
            val process = ProcessBuilder("adb", "logcat", "-v", "time")
                .redirectErrorStream(true)
                .start()

            val reader = process.inputStream.bufferedReader()

            reader.lineSequence().forEach { line ->
                scope.launch(Dispatchers.Main) { addTerminalLine(line) }
            }
        }
    }
}