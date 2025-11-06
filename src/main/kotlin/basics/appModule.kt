package lorry.basics

import io.github.lowley.receiver.DeviceAPI
import io.github.lowley.receiver.IDeviceAPI
import lorry.deviceAPI.DeviceAPIComponent
import lorry.deviceAPI.IDeviceAPIComponent
import lorry.logcat.ILogCatComponent
import lorry.logcat.LogCatComponent
import lorry.ui.ViewerViewModel
import org.koin.dsl.module

val appModule = module {
    single<ILogCatComponent> { LogCatComponent() }
    single<IDeviceAPI> { DeviceAPI() }
    single { ViewerViewModel(get(), get()) }
    single<IDeviceAPIComponent> { DeviceAPIComponent(get()) }
}