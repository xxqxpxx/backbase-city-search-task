package com.example.backbase.data.search.dataSource

import com.example.backbase.data.model.City
import java.util.*

class CityMapper {
    fun fromDisk(diskCities: List<CityDiskModel>): List<City> {
        val cities: MutableList<City> = ArrayList(diskCities.size)
        for (diskCity in diskCities) {
            cities.add(
                City(
                    diskCity.id,
                    diskCity.coordinate,
                    diskCity.country,
                    diskCity.name
                )
            )
        }
        return cities
    }
}