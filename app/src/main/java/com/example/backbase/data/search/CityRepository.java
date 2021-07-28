package com.example.backbase.data.search;


import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.backbase.core.manager.CoroutinesManager;
import com.example.backbase.data.model.City;
import com.example.backbase.data.search.dataSource.CityDiskDataSource;
import com.example.backbase.data.search.dataSource.CityMapper;
import com.example.backbase.data.search.tree.Tree;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CityRepository {
    private final CityDiskDataSource diskDataSource;
    private final CityMapper mapper;
    private final AtomicReference<Tree<City>> trieCache;
    private final AtomicReference<List<City>> allCitiesCache;
    private final CoroutinesManager coroutinesManager;

    public CityRepository(CityDiskDataSource diskDataSource, CityMapper mapper, CoroutinesManager coroutinesManager) {
        this.diskDataSource = diskDataSource;
        this.mapper = mapper;
        this.trieCache = new AtomicReference<>();
        this.allCitiesCache = new AtomicReference<>();
        this.coroutinesManager = coroutinesManager;
    }

    public void getCitiesList(@NonNull final OnCitiesReadyListener listener) {
        if (allCitiesCache.get() != null) {
            listener.onCitiesReady(allCitiesCache.get());
            return;
        }
        if (trieCache.get() != null) {
            AsyncTask.execute(() -> {
                final List<City> cities = trieCache.get().getItems();
                sortAndStoreToCache(cities);
                listener.onCitiesReady(cities);
            });
        } else {
            diskDataSource.parseCities(citiesFromDisk -> {
                final List<City> cities = mapper.fromDisk(citiesFromDisk);
                sortAndStoreToCache(cities);
                Tree<City> tree = new Tree<City>();
                tree.add(cities);
                trieCache.set(tree);
                listener.onCitiesReady(cities);
            });
        }
    }


    private void sortAndStoreToCache(List<City> cities) {
        sortCities(cities);
        allCitiesCache.set(cities);
    }

    private void sortCities(List<City> cities) {
        Collections.sort(cities, (firstCity, secondCity) -> {
            int result = firstCity.getName().compareTo(secondCity.getName());
            if (result != 0) {
                return result;
            }
            return firstCity.getCountry().compareTo(secondCity.getCountry());
        });
    }

    public void search(String name, @NonNull OnSearchDoneListener listener) {
        if (name.isEmpty()) {
            listener.onSearchDone(allCitiesCache.get());
            return;
        }
        AsyncTask.execute(() -> {
            List<City> cities = trieCache.get().autocomplete(name);
            sortCities(cities);
            listener.onSearchDone(cities);
        });

    }

    public interface OnCitiesReadyListener {
        void onCitiesReady(List<City> cities);
    }

    public interface OnSearchDoneListener {
        void onSearchDone(List<City> cities);
    }
}