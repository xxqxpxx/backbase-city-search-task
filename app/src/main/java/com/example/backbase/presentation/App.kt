package com.example.backbase.presentation

import android.app.Application
import com.example.backbase.data.remote.example.fetchDetailsRemoteModule
import com.example.backbase.data.remote.networkModule
import com.example.backbase.presentation.ui.example.fetchDetailsModule
import com.example.backbase.presentation.ui.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App  : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(
                mainModule, fetchDetailsModule, fetchDetailsRemoteModule,
                networkModule
            ))
        }
    }
}