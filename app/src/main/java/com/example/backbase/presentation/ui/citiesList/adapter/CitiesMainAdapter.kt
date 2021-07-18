package com.example.backbase.presentation.ui.citiesList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.backbase.data.model.City
import java.util.*


class CitiesMainAdapter(
    override var callback: CitiesMainAdapterSimpleCallback,
    var  mComparator : Comparator<City>,
    var context: Context,
) : SortedListAdapter<City>(context , City::class.java ,  mComparator , callback) {


    override fun areItemsTheSame(item1: City, item2: City): Boolean {
        return item1._id == item2._id
    }

    override fun areItemContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem;
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater?,
        parent: ViewGroup?,
        viewType: Int
    ): CitiesViewHolder {
        return parent?.let { CitiesViewHolder.from(it) }!!
    }


}

