package lorry.deviceAPI

import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lorry.attention.RichLogEvent
import lorry.ui.utils.TerminalLine
import java.net.ServerSocket
import java.net.Socket

class DeviceAPIComponent: IDeviceAPIComponent {

    private var job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var process1: Process? = null
    private var server: ServerSocket? = null
    private var client: Socket? = null


    override fun launchDeviceAPIViewing(addTerminalLine: (TerminalLine) -> Unit) {

        if (job.isActive && server != null)
            return

        scope.launch {
            try{
                process1 = ProcessBuilder("adb", "reverse", "tcp:7777", "tcp:7777")
                    .redirectErrorStream(true)
                    .start()

                val output = process1!!.inputStream.bufferedReader().use { it.readText()}
                val exitCode = process1!!.waitFor()

                if (exitCode != 0)
                    return@launch
            }catch(e: Exception){
                e.printStackTrace()
                return@launch
            }

            try {
                server = ServerSocket(7777)
            }
            catch(ex: Exception){
                ex.printStackTrace()
                return@launch
            }

            println("Server listening on ${server!!.localPort}")

            while(true){
                try {
                    client = server!!.accept()
                    if (client == null)
                        return@launch
                }catch(ex: Exception){
                    ex.printStackTrace()
                    return@launch
                }
                println("Client connected: ${client!!.inetAddress}")

                client.use { cli ->
                    if (cli == null)
                        return@launch

                    val reader = cli.getInputStream().bufferedReader(Charsets.UTF_8)

                    reader.lineSequence().forEach { line ->
                        try{
                            val event = Gson().fromJson(line, RichLogEvent::class.java)
                            println("reçu: $event")
                            val terminalLine = TerminalLine(
                                text = event.timestampMillis.toString() + "|" + event.message,
                                color = Color.Red
                            )
                            addTerminalLine(terminalLine)

                        }
                        catch(ex: Exception){
                            println("json invalide: $line")
                        }
                    }
                }
            }

//            {
//                // flush final
//                if (batch.isNotEmpty()) batch.forEach {
//                    val line = TerminalLine(it, Color.Red)
//                    addTerminalLine(line)
//                }
//            }
        }
    }

    override fun stopDeviceAPIViewing() {
        server?.let {
            process1?.destroy()
            job.cancel() // annule les lectures en cours
            server!!.close()
            client?.close()
            server = null
            client = null
        }
    }
}