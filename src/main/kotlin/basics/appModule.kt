package lorry.basics.Viewer

import io.github.lowley.receiver.DeviceAPI
import io.github.lowley.receiver.IDeviceAPI
import lorry.deviceAPI.DeviceAPIComponent
import lorry.deviceAPI.IDeviceAPIComponent
import lorry.logcat.ILogCatComponent
import lorry.logcat.LogCatComponent
import lorry.ui.ViewerViewModel
import org.koin.dsl.module

val appModule = module {
    single { ViewerViewModel(get(), get()) }

    single<ILogCatComponent> { LogCatComponent() }

    single<IDeviceAPI> { DeviceAPI() }
    single<IDeviceAPIComponent> { DeviceAPIComponent(get()) }
}