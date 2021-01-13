package com.astronout.testalgostudio.di.module

import com.astronout.testalgostudio.ui.main.viewmodel.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get())
    }
}