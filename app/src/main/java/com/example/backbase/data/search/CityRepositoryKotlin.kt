package com.example.backbase.data.search

import com.example.backbase.core.manager.CoroutinesManager
import com.example.backbase.data.model.City
import com.example.backbase.data.search.dataSource.CityDiskDataSource
import com.example.backbase.data.search.dataSource.CityDiskDataSource.OnCitiesParsedListener
import com.example.backbase.data.search.dataSource.CityDiskModel
import com.example.backbase.data.search.dataSource.CityMapper
import com.example.backbase.data.search.tree.Tree
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class CityRepositoryKotlin(
    private val diskDataSource: CityDiskDataSource,
    private val mapper: CityMapper,
    private val coroutinesManager: CoroutinesManager
) {
    private val trieCache: AtomicReference<Tree<City>> = AtomicReference()
    private val allCitiesCache: AtomicReference<List<City>> = AtomicReference()

    fun getCitiesList(listener: OnCitiesReadyListener) {
        if (allCitiesCache.get() != null) {
            listener.onCitiesReady(allCitiesCache.get())
            return
        }
        if (trieCache.get() != null) {
            coroutinesManager.ioScope.launch {
                val cities = trieCache.get()!!.items
                sortAndStoreToCache(cities)
                listener.onCitiesReady(cities)
            }
        } else {
            diskDataSource.parseCities(object : OnCitiesParsedListener {
                override fun onCitiesParse(cities: List<CityDiskModel>) {
                    val cities = mapper.fromDisk(cities)
                    sortAndStoreToCache(cities)
                    val tree = Tree<City>()
                    tree.add(cities)
                    trieCache.set(tree)
                    listener.onCitiesReady(cities)
                }
            })
        }
    }

    private fun sortAndStoreToCache(cities: List<City>) {
        sortCities(cities)
        allCitiesCache.set(cities)
    }

    private fun sortCities(cities: List<City>) {
        Collections.sort(cities) { (_, _, country, name), (_, _, country2, name2) ->
            val result = name.compareTo(name2)
            if (result != 0) {
                return@sort result
            }
            country.compareTo(country2)
        }
    }

    fun search(name: String, listener: OnSearchDoneListener) {
        if (name.isEmpty()) {
            listener.onSearchDone(allCitiesCache.get())
            return
        }
        coroutinesManager.ioScope.launch {
            val cities = trieCache.get()!!.autocomplete(name)
            sortCities(cities)
            listener.onSearchDone(cities)
        }
    }

    interface OnCitiesReadyListener {
        fun onCitiesReady(cities: List<City>)
    }

    interface OnSearchDoneListener {
        fun onSearchDone(cities: List<City>)
    }

}