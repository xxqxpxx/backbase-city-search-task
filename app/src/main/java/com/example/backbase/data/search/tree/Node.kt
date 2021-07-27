package com.example.backbase.data.search.tree

import java.util.*

class Node<T : TreeValueItem?> {
    private val items: MutableSet<T>
    var children: MutableMap<Char, Node<T>>
    var isEnd = false
        private set

    fun markAsEnd(item: T) {
        isEnd = true
        items.add(item)
    }

    fun getItems(): Set<T> {
        return items
    }

    val allItems: List<T>
        get() {
            val listItem: MutableList<T> = ArrayList()
            if (isEnd) {
                listItem.addAll(items)
            }
            if (!children.isEmpty()) {
                for (node in children.values) {
                    listItem.addAll(node.allItems)
                }
            }
            return listItem
        }

    init {
        items = HashSet()
        children = HashMap()
    }
}