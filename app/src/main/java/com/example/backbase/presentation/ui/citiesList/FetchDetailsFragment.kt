package com.example.backbase.presentation.ui.citiesList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backbase.data.model.City
import com.example.backbase.databinding.FragmentFetchDetailsBinding
import com.example.backbase.presentation.App
import com.example.backbase.presentation.ui.citiesList.adapter.CitiesMainAdapter
import com.example.backbase.presentation.ui.citiesList.adapter.CitiesMainAdapterSimpleCallback
import org.koin.android.ext.android.inject
import java.util.*


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

        vm.setCityRepository((requireActivity().application as App).getCityRepository())

        vm.start()
    }

    private fun initObservers() {
        vm.updateEvent.observe(viewLifecycleOwner, {
            // Update UI here
        })

        vm.updateCitiesList.observe(viewLifecycleOwner, { showItems(it) })

        vm.updateCitiesListAfterFilter.observe(viewLifecycleOwner, { updateListAfterFilter(it) })

        vm.updateCitiesListNew.observe(viewLifecycleOwner, { showItems(it) })

        vm.progressVisible.observe(viewLifecycleOwner) { handleProgress(it) }

    }

    private fun handleProgress(show : Boolean) {
        if (show){
            binding.progressLayout.visibility = View.VISIBLE
        }else{
            binding.progressLayout.visibility = View.GONE
        }
    }

    private fun showItems(arrayOfCitys: List<City>) {
        initRecyclerView(ArrayList(arrayOfCitys))
        initSearch()
    }

    private fun showItems(arrayOfCitys: ArrayList<City>) {
        initRecyclerView(arrayOfCitys)
        initSearch()
    }

    private fun initSearch() {
        binding.searchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(searchQuery: Editable) {
                vm.search(searchQuery.trim().toString());
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(searchQuery: CharSequence, start: Int, before: Int, count: Int) {
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
