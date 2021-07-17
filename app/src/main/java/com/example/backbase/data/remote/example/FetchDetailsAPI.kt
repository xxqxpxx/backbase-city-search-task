package com.example.backbase.data.remote.example

import com.example.backbase.data.remote.model.example.DogDetailsResponse
import retrofit2.http.GET

interface FetchDetailsAPI {
        @GET("/api/breeds/image/random")
        suspend fun fetchDetails(): DogDetailsResponse
}