package com.example.backbase.presentation.ui.citiesList

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.backbase.R
import com.example.backbase.core.manager.CoroutinesManager
import com.example.backbase.data.model.City
import com.example.backbase.data.remote.example.FetchDetailsRepo
import com.example.backbase.presentation.livedata.SingleLiveEvent
import com.example.backbase.presentation.ui.utils.ResourceProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FetchDetailsViewModel(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val fetchDetailsRepo: FetchDetailsRepo
) : ViewModel() {

    companion object {
        private const val logTag = "SkeletonRepository"
    }

    val updateEvent = SingleLiveEvent<Boolean>()
    val textObservable = ObservableField<String>()


    val updateCitiesList = SingleLiveEvent<ArrayList<City>>()
    val updateCitiesListAfterFilter = SingleLiveEvent<ArrayList<City>>()

    private lateinit var citiesList: ArrayList<City>
    private val mComparator: Comparator<City> = Comparator { a, b -> a.name.compareTo(b.name) }


    fun makeNetworkCall() {
        Log.i(logTag, "Set TextView using DataBinding")
        textObservable.set(resourceProvider.getString(R.string.info_txt))

        coroutinesManager.ioScope.launch {
            val deferredList = ArrayList<Deferred<*>>()

            Log.i(logTag, "Make 10 concurrent network calls")
            for (i in 0..10) {

                deferredList.add(async {
                    Log.i(logTag, "Network Call ID: $i")
                    fetchDetailsRepo.fetchDetails()
                })
            }

            deferredList.joinAll()
            Log.i(logTag, "All Networks calls complete")

            updateEvent.postValue(true)
            Log.i(logTag, "Update UI")
        }
    }



    fun getCitiesList(context: Context) {
        coroutinesManager.ioScope.launch {
            val gson = Gson()
            val arrayType = object : TypeToken<Array<City>>() {}.type
            val citiesObject: Array<City> = gson.fromJson(getJsonDataFromAsset(context, "cities.json"), arrayType)
            citiesList = citiesObject.toCollection(java.util.ArrayList())
            updateCitiesList.postValue(citiesObject.toCollection(java.util.ArrayList()))
        }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
        return jsonString
    }

    fun onQueryTextChange(query: String) {
        coroutinesManager.ioScope.launch {
            if(!citiesList.isNullOrEmpty()) {
                val filteredModelList = filter(citiesList, query)
                updateCitiesListAfterFilter.postValue(filteredModelList)
            }
        }
    }

    private fun filter(models: List<City>, query: String): java.util.ArrayList<City> {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        val filteredModelList: java.util.ArrayList<City> = java.util.ArrayList<City>()
        for (model in models) {
            val text: String = model.name.lowercase(Locale.getDefault())
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    fun getComparator(): Comparator<City> {
        return mComparator
    }

}
