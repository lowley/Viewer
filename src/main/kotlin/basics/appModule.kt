package lorry.basics.Viewer

import lorry.deviceAPI.DeviceAPIComponent
import lorry.deviceAPI.IDeviceAPIComponent
import lorry.logcat.ILogCatComponent
import lorry.logcat.LogCatComponent
import lorry.ui.ViewerViewModel
import org.koin.dsl.module

val appModule = module {
    single { ViewerViewModel(get(), get()) }
    single<ILogCatComponent> { LogCatComponent() }
    single<IDeviceAPIComponent> { DeviceAPIComponent() }
}