package lorry.deviceAPI

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import io.github.lowley.common.AdbError
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

                        val builder: AnnotatedString.Builder = AnnotatedString.Builder()

                        event.richText.richSegments.forEach { segment ->
                            with(segment.style) {

                                val finalStyle = SpanStyle(
                                    fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                                    textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None,
                                )

                                builder.withStyle(style = finalStyle) {
                                    append(text = segment.text.text)
                                }
                            }
                        }

                        TerminalLine(
                            text = builder.toAnnotatedString(),
                            color = Color.Red
                        )
                    }.collect { terminalLine -> addTerminalLine(terminalLine) }
                }
            }
        )
    }

    private fun errorInDeviceCommunication(error: AdbError) {
        when (error) {
            is AdbError.ExceptionThrown -> println("téléphone communication exception: ${error.throwable.message}")
            is AdbError.CommandFailed -> println("téléphone communication erreur: ${error.output}")
        }
    }

    override fun stopDeviceAPIViewing() {
        readingJob?.cancel()
        readingJob = null
        deviceAPI.close()
    }
}