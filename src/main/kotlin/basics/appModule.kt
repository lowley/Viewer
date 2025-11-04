package lorry.basics

import lorry.logcat.ILogCatComponent
import lorry.logcat.LogCatComponent
import lorry.ui.ViewerViewModel
import org.koin.dsl.module

val appModule = module {
    single<ILogCatComponent> { LogCatComponent() }
    single { ViewerViewModel(get()) }
}