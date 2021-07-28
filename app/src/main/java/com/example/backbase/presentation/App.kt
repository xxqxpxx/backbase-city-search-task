package com.example.backbase.presentation

import android.app.Application
import com.example.backbase.core.manager.CoroutinesManager
import com.example.backbase.core.utils.FileHelper
import com.example.backbase.data.search.CityRepository
import com.example.backbase.data.search.dataSource.CityDiskDataSource
import com.example.backbase.data.search.dataSource.CityMapper
import com.example.backbase.presentation.ui.citiesList.fetchDetailsModule
import com.example.backbase.presentation.ui.mainModule
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.atomic.AtomicReference


class App : Application() {

    private var cityRepositoryKotlin: AtomicReference<CityRepository> = AtomicReference()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(mainModule, fetchDetailsModule))
        }

        cityRepositoryKotlin = AtomicReference()

    }


    fun getCityRepository(): CityRepository {
        if (cityRepositoryKotlin.get() != null) {
            return cityRepositoryKotlin.get()
        }

        val fileHelper = provideFileHelper()
        val coroutinesManager = CoroutinesManager()

        val diskDataSource = provideCityDiskDataSource(fileHelper, coroutinesManager)
        val repository = CityRepository(diskDataSource, CityMapper(), coroutinesManager)
        cityRepositoryKotlin.set(repository)
        return repository
    }

    private fun provideCityDiskDataSource(
        fileHelper: FileHelper,
        coroutinesManager: CoroutinesManager
    ): CityDiskDataSource {
        return CityDiskDataSource(fileHelper, Gson(), coroutinesManager)
    }

    private fun provideFileHelper(): FileHelper {
        return FileHelper(this)
    }
}