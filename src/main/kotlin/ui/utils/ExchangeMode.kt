package lorry.ui.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lorry.basics.getAllOf

sealed class ExchangeMode(val name: String, val index: Int) {

    data object LogCat : ExchangeMode("Logcat", 2)
    data object DeviceAPI : ExchangeMode("Application", 3)
    data object Both : ExchangeMode("Les 2", 4)
    data object None: ExchangeMode("Aucun", 1)
}

/**
 * #[[ExchangeModeChooser]]
 */
context(scope: RowScope)
@Composable
fun ExchangeModeChooser(
    modifier: Modifier = Modifier,
    setExchangeMode: (ExchangeMode) -> Unit,
    stopLogcatViewing: () -> Unit,
    enableAndroidLogs: (Boolean) -> Unit
) = with(scope) {

    var selectedIndex by remember { mutableIntStateOf(0) }
    val exchangeModes = getAllOf<ExchangeMode>()

    Text(
        modifier = modifier
            .align(Alignment.CenterVertically),
        text = "Connexion à"
    )

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .padding(start = 10.dp)
            .height(35.dp),
    ) {
        exchangeModes.sortedBy { it.index }.forEachIndexed { index, exchangeMode ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = exchangeModes.count()
                ),
                onClick = {
                    if (selectedIndex != index) {
                        selectedIndex = index
                        setExchangeMode(exchangeMode)
                        enableAndroidLogs(exchangeMode == ExchangeMode.DeviceAPI
                                || exchangeMode == ExchangeMode.Both)
                    }
                    else {
                        selectedIndex = 0
                        setExchangeMode(ExchangeMode.None)
                        enableAndroidLogs(false)
                    }

                },
                selected = index == selectedIndex,
                label = { Text(exchangeMode.name) }
            )
        }
    }
}