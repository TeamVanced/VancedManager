package com.vanced.manager.di

import com.vanced.manager.ui.viewmodel.InstallViewModel
import com.vanced.manager.ui.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), androidApplication()) }
    viewModel { InstallViewModel(get(), get(), get(), get(), get(), get()) }
}