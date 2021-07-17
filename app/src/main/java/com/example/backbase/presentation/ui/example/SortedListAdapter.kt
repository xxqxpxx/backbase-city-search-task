package com.example.backbase.presentation.ui.example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.backbase.data.model.City
import com.example.backbase.databinding.CityListItemBinding
import java.util.*


abstract class SortedListAdapter<T : SortedListAdapter.ViewModel?>
    (context: Context, itemClass: Class<T>, comparator: Comparator<T>, open var callback: CitiesMainAdapterSimpleCallback
) : RecyclerView.Adapter<SortedListAdapter.CitiesViewHolder>() {
    interface Editor<T : ViewModel?> {
        fun add(item: T): Editor<T>
        fun add(items: List<T>?): Editor<T>
        fun remove(item: T): Editor<T>
        fun remove(items: List<T>): Editor<T>
        fun replaceAll(items: List<T>): Editor<T>
        fun removeAll(): Editor<T>
        fun commit()
    }

    interface Filter<T> {
        fun test(item: T): Boolean
    }

    private val mInflater: LayoutInflater
    private val mSortedList: SortedList<T>
    private val mComparator: Comparator<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder.from(parent)
    }

    protected abstract fun onCreateViewHolder(
        inflater: LayoutInflater?,
        parent: ViewGroup?,
        viewType: Int
    ): CitiesViewHolder

    protected abstract fun areItemsTheSame(item1: T, item2: T): Boolean

    protected abstract fun areItemContentsTheSame(oldItem: T, newItem: T): Boolean

    /*override fun onBindViewHolder(holder: ViewHolder< T>, position: Int) {
        val item = mSortedList[position]
        (holder as ViewHolder<T>).bind(item)
    }

    */

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        with(holder) {

        //    val context = binding.itemCityTitle.context
         //   val resources = binding.itemCityTitle.resources

            val item = mSortedList[position] as City


            binding.itemCityTitle.text = item.name
            binding.itemCitySubtite.text = item.country

            binding.itemContainer.setOnClickListener {
                callback.onCityItemClicked(position)
            }

        }
    }

    fun edit(): Editor<T> {
        return EditorImpl()
    }

    fun getItem(position: Int): T {
        return mSortedList[position]
    }

    override fun getItemCount(): Int {
        return mSortedList.size()
    }

    fun filter(filter: Filter<T>): List<T> {
        val list: MutableList<T> = ArrayList()
        var i = 0
        val count = mSortedList.size()
        while (i < count) {
            val item = mSortedList[i]
            if (filter.test(item)) {
                list.add(item)
            }
            i++
        }
        return list
    }

    fun filterOne(filter: Filter<T>): T? {
        var i = 0
        val count = mSortedList.size()
        while (i < count) {
            val item = mSortedList[i]
            if (filter.test(item)) {
                return item
            }
            i++
        }
        return null
    }

    private interface Action<T : ViewModel?> {
        fun perform(list: SortedList<T>?)
    }

    private inner class EditorImpl : Editor<T> {
        private val mActions: MutableList<Action<T>> = ArrayList()
        override fun add(item: T): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>?) {
                    mSortedList.add(item)
                }
            })
            return this
        }

        override fun add(items: List<T>?): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>?) {
                    Collections.sort(items, mComparator)
                    mSortedList.addAll(items!!)
                }
            })
            return this
        }

        override fun remove(item: T): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>?) {
                    mSortedList.remove(item)
                }
            })
            return this
        }

        override fun remove(items: List<T>): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>?) {
                    for (item in items) {
                        mSortedList.remove(item)
                    }
                }
            })
            return this
        }

        override fun replaceAll(items: List<T>): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>?) {
                    val itemsToRemove = filter(object : Filter<T> {
                        override fun test(item: T): Boolean {
                            return !items.contains(item)
                        }
                    })
                    for (i in itemsToRemove.indices.reversed()) {
                        val item = itemsToRemove[i]
                        mSortedList.remove(item)
                    }
                    mSortedList.addAll(items)
                }
            })
            return this
        }

        override fun removeAll(): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>?) {
                    mSortedList.clear()
                }
            })
            return this
        }

        override fun commit() {
            mSortedList.beginBatchedUpdates()
            for (action in mActions) {
                action.perform(mSortedList)
            }
            mSortedList.endBatchedUpdates()
            mActions.clear()
        }
    }

    abstract class ViewHolder<T : ViewModel?>(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        var currentItem: T? = null
            private set

        fun bind(item: T) {
            currentItem = item
            performBind(item)
        }

        protected abstract fun performBind(item: T)
    }

    interface ViewModel

    init {
        mInflater = LayoutInflater.from(context)
        mComparator = comparator
        mSortedList = SortedList(itemClass, object : SortedList.Callback<T>() {
            override fun compare(a: T, b: T): Int {
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

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return areItemContentsTheSame(oldItem, newItem)
            }

            override fun areItemsTheSame(item1: T, item2: T): Boolean {
                return this@SortedListAdapter.areItemsTheSame(item1, item2)
            }
        })
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

}

interface CitiesMainAdapterSimpleCallback {
    fun onCityItemClicked(position: Int)
}
