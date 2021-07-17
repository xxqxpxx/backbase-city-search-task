package com.example.backbase.presentation.ui.example

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backbase.data.model.City
import com.example.backbase.databinding.FragmentFetchDetailsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject
import java.io.IOException
import java.util.*
import kotlin.Array
import kotlin.Boolean
import kotlin.CharSequence
import kotlin.Comparator
import kotlin.Int
import kotlin.String
import kotlin.getValue


class FetchDetailsFragment : Fragment() , CitiesMainAdapterSimpleCallback{

    private val vm by inject<FetchDetailsViewModel>()

    private lateinit var binding: FragmentFetchDetailsBinding

    private lateinit var citiesMainAdapter : CitiesMainAdapterSimple

    private val mComparator: Comparator<City> = Comparator { a, b -> a.name.compareTo(b.name) }

    private lateinit var categoryList:ArrayList<City>

    companion object {
        const val TAG  = "FetchDogsFragment"
        fun newInstance() =
            FetchDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFetchDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        vm.makeNetworkCall()

        val gson = Gson()

        val arrayTutorialType = object : TypeToken<Array<City>>() {}.type

        val tutorials: Array<City> = gson.fromJson(getJsonDataFromAsset(requireContext() , "cities.json"), arrayTutorialType)

         categoryList = tutorials.toCollection(ArrayList())

        showItems(categoryList)
    }

    private fun showItems(arrayOfCitys: ArrayList<City>) {
        initRecyclerView(arrayOfCitys)

        initSearch()
    }

    private fun initSearch() {

        binding.searchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                onQueryTextChange( s.toString())
            }
        })



    }


    private fun initRecyclerView(arrayOfCitys: ArrayList<City>) {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager

        citiesMainAdapter = CitiesMainAdapterSimple( this , mComparator , requireContext())

        citiesMainAdapter.edit()
            .replaceAll(arrayOfCitys)
            .commit();

        binding.mainList.adapter = citiesMainAdapter

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


    fun onQueryTextChange(query: String): Boolean {
        val filteredModelList: ArrayList<City> = filter(categoryList, query)
    //    citiesMainAdapter.replaceAll(filteredModelList)

        citiesMainAdapter.edit().replaceAll(filteredModelList).commit()

        binding.mainList.scrollToPosition(0)
        return true
    }

    private fun filter(models: List<City>, query: String): ArrayList<City> {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        val filteredModelList: ArrayList<City> = ArrayList<City>()
        for (model in models) {
            val text: String = model.name.lowercase(Locale.getDefault())
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }



    fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }


    private fun initObservers() {
        vm.updateEvent.observe(viewLifecycleOwner, Observer {
            // Update UI here
        })
    }

    override fun onCityItemClicked(position: Int) {
        goToMap(position)
    }

    private fun goToMap(position: Int) {
        Toast.makeText(requireContext() , "going to map" + position.toString() , Toast.LENGTH_LONG).show()
    }

}
