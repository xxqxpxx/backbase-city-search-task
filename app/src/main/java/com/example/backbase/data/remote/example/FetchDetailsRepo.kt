package com.example.backbase.data.remote.example

import com.example.backbase.core.extensions.handleException
 import com.example.backbase.data.remote.model.Resource
import com.example.backbase.data.remote.model.example.DogDetailsResponse

class FetchDetailsRepo(private val fetchDetailsAPI: FetchDetailsAPI) {
    suspend fun fetchDetails(): Resource<DogDetailsResponse> {
        return handleException { fetchDetailsAPI.fetchDetails() }
    }
}