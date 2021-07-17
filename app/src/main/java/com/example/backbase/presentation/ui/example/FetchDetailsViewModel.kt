package com.example.backbase.presentation.ui.example

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.backbase.R
import com.example.backbase.core.manager.CoroutinesManager
import com.example.backbase.data.remote.example.FetchDetailsRepo
import com.example.backbase.presentation.livedata.SingleLiveEvent
import com.example.backbase.presentation.ui.utils.ResourceProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class FetchDetailsViewModel(
    private val resourceProvider: ResourceProvider,
    private val coroutinesManager: CoroutinesManager,
    private val fetchDetailsRepo: FetchDetailsRepo
) : ViewModel() {

    companion object {
        private const val logTag = "SkeletonRepository"
    }

    val updateEvent = SingleLiveEvent<Boolean>()
    val textObservable = ObservableField<String>()

    fun makeNetworkCall() {
        Log.i(logTag, "Set TextView using DataBinding")
        textObservable.set(resourceProvider.getString(R.string.info_txt))

        coroutinesManager.ioScope.launch {
            val deferredList = ArrayList<Deferred<*>>()

            Log.i(logTag, "Make 10 concurrent network calls")
            for (i in 0..10) {

                deferredList.add(async {
                    Log.i(logTag, "Network Call ID: $i")
                    fetchDetailsRepo.fetchDetails()
                })
            }

            deferredList.joinAll()
            Log.i(logTag, "All Networks calls complete")

            updateEvent.postValue(true)
            Log.i(logTag, "Update UI")
        }
    }
}
