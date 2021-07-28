package com.example.backbase.presentation.ui.citiesList

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.backbase.data.model.City
import com.example.backbase.data.search.CityRepository
import com.example.backbase.presentation.livedata.SingleLiveEvent
import java.util.*

class FetchDetailsViewModel : ViewModel() {

    private lateinit var cityRepositoryKotlin: CityRepository


    private val mComparator: Comparator<City> = Comparator { a, b -> a.name.compareTo(b.name) }


    var cities = ObservableField<List<City>>(Collections.emptyList())
    var progressVisible = SingleLiveEvent<Boolean>()
    val updateCitiesListNew = SingleLiveEvent<List<City>>()


    fun setCityRepository(cityRepositoryKotlin: CityRepository) {
        this.cityRepositoryKotlin = cityRepositoryKotlin
    }

    private fun retrieveCities() {
        cityRepositoryKotlin.getCitiesList { citiesList ->
            cities.set(citiesList)
            progressVisible.postValue(false)
            updateCitiesListNew.postValue(cities.get())
        }
    }

    fun search(searchQuery: String) {
        cityRepositoryKotlin.search(searchQuery, cities::set)
        updateCitiesListNew.postValue(cities.get())
    }

    fun getComparator(): Comparator<City> {
        return mComparator
    }

    fun start() {
        retrieveCities()
    }

}
