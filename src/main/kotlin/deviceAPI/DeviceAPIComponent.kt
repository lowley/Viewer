package lorry.deviceAPI

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import io.github.lowley.common.AdbError
import io.github.lowley.engineRoom.boat.SurfaceLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import lorry.ui.utils.TerminalLine

class DeviceAPIComponent(
) : IDeviceAPIComponent {

    var readingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun launchDeviceAPIViewing(addTerminalLine: (TerminalLine) -> Unit) {
        if (readingJob != null)
            return

        readingJob = scope.launch {
            SurfaceLogging.logFlow.map { event ->

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


    private fun errorInDeviceCommunication(error: AdbError) {
        when (error) {
            is AdbError.ExceptionThrown -> println("téléphone communication exception: ${error.throwable.message}")
            is AdbError.CommandFailed -> println("téléphone communication erreur: ${error.output}")
        }
    }

    override fun stopDeviceAPIViewing() {
        readingJob?.cancel()
        readingJob = null
    }
}