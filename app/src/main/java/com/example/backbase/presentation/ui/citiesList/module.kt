package com.example.backbase.presentation.ui.citiesList

import com.example.backbase.core.manager.CoroutinesManager
import com.example.backbase.presentation.ui.utils.ResourceProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fetchDetailsModule = module {
    viewModel { FetchDetailsViewModel(get(), get(), get()  ) }
    single { ResourceProvider(androidApplication()) }
    single { CoroutinesManager() }
}