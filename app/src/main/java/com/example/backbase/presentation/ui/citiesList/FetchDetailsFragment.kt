package com.example.backbase.presentation.ui.citiesList

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
import com.example.backbase.presentation.ui.citiesList.adapter.CitiesMainAdapter
import com.example.backbase.presentation.ui.citiesList.adapter.CitiesMainAdapterSimpleCallback
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.Boolean
import kotlin.CharSequence
import kotlin.Comparator
import kotlin.Int
import kotlin.String
import kotlin.getValue


class FetchDetailsFragment : Fragment() , CitiesMainAdapterSimpleCallback {

    private val vm by inject<FetchDetailsViewModel>()

    private lateinit var binding: FragmentFetchDetailsBinding
    private lateinit var citiesMainAdapter : CitiesMainAdapter

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

        vm.getCitiesList(requireContext())

    }

    private fun initObservers() {
        vm.updateEvent.observe(viewLifecycleOwner, {
            // Update UI here
        })

        vm.updateCitiesList.observe(viewLifecycleOwner, { showItems(it) })

        vm.updateCitiesListAfterFilter.observe(viewLifecycleOwner, { updateListAfterFilter(it) })


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
                vm.onQueryTextChange( s.toString())
            }
        })


    }


    private fun initRecyclerView(arrayOfCitys: ArrayList<City>) {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager
        citiesMainAdapter = CitiesMainAdapter( this , vm.getComparator() , requireContext())
        citiesMainAdapter.edit().replaceAll(arrayOfCitys).commit();
        binding.mainList.adapter = citiesMainAdapter
    }

    private fun updateListAfterFilter(filteredModelList: ArrayList<City> ): Boolean {
        citiesMainAdapter.edit().replaceAll(filteredModelList).commit()
        binding.mainList.scrollToPosition(0)
        return true
    }


    override fun onCityItemClicked(position: Int) {
        goToMap(position)
    }

    private fun goToMap(position: Int) {
        Toast.makeText(requireContext() , "going to map" + position.toString() , Toast.LENGTH_LONG).show()
    }

}
