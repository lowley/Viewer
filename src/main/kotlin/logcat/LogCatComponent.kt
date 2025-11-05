package lorry.logcat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lorry.ui.utils.TerminalLine
import androidx.compose.ui.graphics.Color


class LogCatComponent : ILogCatComponent {

    private var job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var process: Process? = null

    override fun launchLogCatViewing(addTerminalLine: (TerminalLine) -> Unit) {

        if (job.isActive && process != null)
            return

        job = SupervisorJob()

        scope.launch {
            process = ProcessBuilder("adb", "logcat", "-v", "time")
                .redirectErrorStream(true)
                .start()

            process!!.inputStream.bufferedReader().useLines { seq ->
                val batch = mutableListOf<String>()
                var lastFlush = System.nanoTime()
                for (line in seq) {
                    batch += line
                    val now = System.nanoTime()
                    val shouldFlush = batch.size >= 100 || (now - lastFlush) > 150_000_000L // ~150ms
                    if (shouldFlush) {
                        val toSend = batch.toList();
                        batch.clear();
                        lastFlush = now
                        // Ici, appelez une API batch du VM si possible.
                        toSend.forEach {
                            val line = TerminalLine(it, Color.Blue)
                            addTerminalLine(line)
                        }
                    }
                }
                // flush final
                if (batch.isNotEmpty()) batch.forEach {
                    val line = TerminalLine(it, Color.Blue)
                    addTerminalLine(line)
                }
            }
        }
    }

    override fun stopLogcatViewing() {
        process?.let {
            it.destroy()
            job.cancel() // annule les lectures en cours
            process = null
        }
    }
}