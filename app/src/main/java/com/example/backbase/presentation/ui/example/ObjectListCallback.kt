package com.example.backbase.presentation.ui.example

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.example.backbase.data.model.City
import java.util.*


class ObjectListCallback(adapter: RecyclerView.Adapter<*>) : SortedListAdapterCallback<City>(adapter) {

    private val ALPHABETICAL_COMPARATOR: Comparator<City> = Comparator<City> { a, b -> a.name.compareTo(b.name) }

    override fun compare(a: City, b: City): Int {
        return ALPHABETICAL_COMPARATOR.compare(a, b);

    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem;
    }

    override fun areItemsTheSame(item1: City, item2: City): Boolean {
        return item1._id == item2._id
    }
}