package com.example.backbase.presentation.ui.citiesList

import com.example.backbase.core.manager.CoroutinesManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fetchDetailsModule = module {
    viewModel { FetchDetailsViewModel() }
    single { CoroutinesManager() }
}