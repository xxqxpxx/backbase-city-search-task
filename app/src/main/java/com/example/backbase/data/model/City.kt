package com.example.backbase.data.model

import com.example.backbase.presentation.ui.citiesList.adapter.SortedListAdapter

data class City(
    val _id: Int,
    val coord: Coord,
    val country: String,
    val name: String
) : SortedListAdapter.ViewModel
