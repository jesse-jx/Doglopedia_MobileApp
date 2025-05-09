package com.example.doglopedia.network

import com.example.doglopedia.models.DogBreeds
import retrofit2.http.GET

interface DogApiService {
    interface DogApiService {
        @GET("v1/breeds")
        suspend fun getBreeds(): List<DogBreeds>
    }
}