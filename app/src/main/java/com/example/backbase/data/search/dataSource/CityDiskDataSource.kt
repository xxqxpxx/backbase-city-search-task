package com.example.backbase.data.search.dataSource

import android.text.TextUtils
import com.example.backbase.R
import com.example.backbase.core.manager.CoroutinesManager
import com.example.backbase.core.utils.FileHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.*

class CityDiskDataSource(
    private val fileHelper: FileHelper,
    private val gson: Gson,
    private val coroutinesManager: CoroutinesManager
) {
    fun parseCities(listener: OnCitiesParsedListener) {
        coroutinesManager.ioScope.launch {
            val citiesJson = fileHelper.read(R.raw.cities)
            var cities: List<CityDiskModel> = emptyList()
            if (!TextUtils.isEmpty(citiesJson)) {
                val listType = object : TypeToken<ArrayList<CityDiskModel>>() {}.type
                cities = gson.fromJson(citiesJson, listType)
            }
            listener.onCitiesParse(cities)
        }
    }

    interface OnCitiesParsedListener {
        fun onCitiesParse(cities: List<CityDiskModel>)
    }
}