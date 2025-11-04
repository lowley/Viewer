package lorry.basics

import lorry.ui.ViewerViewModel
import org.koin.dsl.module

val appModule = module {
    single { ViewerViewModel() }
//    single { UseCase(get()) }
}