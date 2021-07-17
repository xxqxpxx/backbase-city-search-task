package com.example.backbase.data.remote.example

import com.example.backbase.data.remote.createWebService
import com.example.backbase.data.remote.provideRetrofit
import org.koin.dsl.module


val fetchDetailsRemoteModule = module {

    single {
        provideRetrofit(
            get(),
            "https://dog.ceo"
        )
    }

    single { FetchDetailsRepo(get()) }

    factory {
        createWebService<FetchDetailsAPI>(
            get()
        )
    }
}