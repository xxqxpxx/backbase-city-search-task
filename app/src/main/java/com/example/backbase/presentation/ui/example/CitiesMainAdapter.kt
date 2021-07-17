package com.example.backbase.presentation.ui.example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.backbase.data.model.City
import com.example.backbase.databinding.CityListItemBinding


class CitiesMainAdapter(
    var filterItems: ArrayList<City>,
    var callback: CitiesMainAdapterCallback ,
     mComparator : Comparator<City>
) : RecyclerView.Adapter<CitiesMainAdapter.CitiesViewHolder>() {

    private val ALPHABETICAL_COMPARATOR: java.util.Comparator<City> = Comparator<City> { a, b -> a.name.compareTo(b.name) }


    private val mSortedList: SortedList<City> =
        SortedList(City::class.java, object : SortedList.Callback<City>() {
            override fun compare(a: City, b: City): Int {
                return mComparator.compare(a, b)
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: City, item2: City): Boolean {
                return item1._id == item2._id
            }
        })


    fun add(model: City) {
        mSortedList.add(model)
    }

    fun remove(model: City) {
        mSortedList.remove(model)
    }

    fun add(models: List<City>) {
        mSortedList.addAll(models)
    }

    fun remove(models: List<City>) {
        mSortedList.beginBatchedUpdates()
        for (model in models) {
            mSortedList.remove(model)
        }
        mSortedList.endBatchedUpdates()
    }

    fun replaceAll(models: List<City>) {
        mSortedList.beginBatchedUpdates()
        for (i in mSortedList.size() - 1 downTo 0) {
            val model: City = mSortedList[i]
            if (!models.contains(model)) {
                mSortedList.remove(model)
            }
        }
        mSortedList.addAll(models)
        mSortedList.endBatchedUpdates()
    }

  /*  override fun getItemCount(): Int {
        return filterItems.size
    }*/

    override fun getItemCount(): Int {
        return mSortedList.size()
    }


    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        with(holder) {

            val context = binding.itemCityTitle.context
            val resources = binding.itemCityTitle.resources

            binding.itemCityTitle.text = mSortedList[position].name
            binding.itemCitySubtite.text = mSortedList[position].country

            binding.itemContainer.setOnClickListener {
                callback.onCityItemClicked(position)
            }

        }
    }


    class CitiesViewHolder(var binding: CityListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): CitiesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CityListItemBinding.inflate(inflater, parent, false)
                return CitiesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder.from(parent)
    }

}

interface CitiesMainAdapterCallback {
    fun onCityItemClicked(position: Int)
}
