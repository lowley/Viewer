package lorry.deviceAPI

import androidx.compose.ui.graphics.Color
import io.github.lowley.receiver.DeviceAPI
import io.github.lowley.receiver.IDeviceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import lorry.ui.utils.TerminalLine

class DeviceAPIComponent(
    val deviceAPI: IDeviceAPI
) : IDeviceAPIComponent {

    var readingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun launchDeviceAPIViewing(addTerminalLine: (TerminalLine) -> Unit) {
        if (readingJob != null)
            return

        val eventFlow = deviceAPI.deviceLogEvents()
        eventFlow.fold(
            ifLeft = { error -> errorInDeviceCommunication(error = error) },
            ifRight = { flow ->
                readingJob = scope.launch {
                    flow.map { event ->
                        TerminalLine(
                            text = "${event.timestampMillis}|${event.message}",
                            color = Color.Red
                        )
                    }.collect { terminalLine -> addTerminalLine(terminalLine) }
                }
            }
        )
    }

    private fun errorInDeviceCommunication(error: DeviceAPI.AdbError) {
        when (error){
            is DeviceAPI.AdbError.ExceptionThrown -> println ("téléphone communication exception: ${error.throwable.message}")
            is DeviceAPI.AdbError.CommandFailed -> println ("téléphone communication erreur: ${error.output}")
        }
    }

    override fun stopDeviceAPIViewing() {
        readingJob?.cancel()
        readingJob = null
    }
}