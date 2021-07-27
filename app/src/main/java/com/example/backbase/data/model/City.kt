package com.example.backbase.data.model

import com.example.backbase.data.search.tree.TreeValueItem
import com.example.backbase.presentation.ui.citiesList.adapter.SortedListAdapter
import java.util.*

data class City (
    val _id: Int,
    val coord: Coord,
    val country: String,
    val name: String
) : SortedListAdapter.ViewModel , TreeValueItem {

    override val value: String
        get() =  name.lowercase(Locale.getDefault())
}
