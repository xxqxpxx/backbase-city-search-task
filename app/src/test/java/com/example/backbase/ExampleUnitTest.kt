package com.example.backbase

import com.example.backbase.data.model.City
import com.example.backbase.data.model.Coord
import com.example.backbase.data.search.tree.Tree
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun searchCity_empty () {
        val Tree: Tree<City> = Tree()
        val countries: List<City> = buildCities()
        for (city in countries) {
            Tree.add(city)
        }
        assertEquals(0, Tree.autocomplete("Cairo").size)
    }

    @Test
    fun searchCity_result() {
        val tree: Tree<City> = Tree()
        val countries: List<City> = buildCities()
        for (city in countries) {
            tree.add(city)
        }
        assertEquals(1, tree.autocomplete("Alabama").size)
    }

    private fun buildCities(): MutableList<City> {
        val cities: MutableList<City> = ArrayList()
        cities.add(City(1, Coord(-86.750259, 32.750408), "US", "Alabama"))
        cities.add(City(2, Coord(-86.750259, 32.750408), "US", "Albuquerque"))
        cities.add(City(3, Coord(-86.750259, 32.750408), "US", "Anaheim"))
        cities.add(City(4, Coord(-86.750259, 32.750408), "US", "Arizona"))
        cities.add(City(5, Coord(-86.750259, 32.750408), "US", "Sydney"))
        return cities
    }
}