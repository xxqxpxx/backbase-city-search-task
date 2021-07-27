package com.example.backbase.data.search.tree

import java.util.*

class Tree<T : TreeValueItem?> {

    private val root: Node<T> = Node()

    val items: List<T>
        get() = root.allItems

    fun add(items: List<T>) {
        for (item in items) {
            add(item)
        }
    }

    fun add(item: T) {
        add(item!!.value, item, root)
    }

    private fun add(value: String, item: T, node: Node<T>?) {
        if (value.isEmpty()) {
            node!!.markAsEnd(item)
            return
        }
        val c = value[0]
        val child: Node<T>?
        if (!node!!.children.containsKey(c)) {
            child = Node()
            node.children[c] = child
        } else {
            child = node.children[c]
        }
        add(value.substring(1), item, child)
    }

    fun autocomplete(prefix: String): List<T> {
        val formattedPrefix = prefix.lowercase(Locale.getDefault())
        var node = root
        var letter: Char
        for (element in formattedPrefix) {
            letter = element
            node = if (node.children.containsKey(letter)) {
                node.children[letter]!!
            } else {
                return emptyList()
            }
        }
        return node.allItems
    }
}