package com.example.doglopedia.network

import com.example.doglopedia.models.Dog
import com.example.doglopedia.models.Image
import com.example.doglopedia.utils.CONSTANTS
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {
    @GET("breeds")
    suspend fun getDogs(
        @Query("api_key")
        apiKey: String = CONSTANTS.API_KEY
    ): List<Dog>

    @GET("images/search")
    suspend fun getDogImagesById(
        @Query("breed_id")
        id: Long,
        @Query("limit")
        limit: Int = 100,
        @Query("api_key")
        apiKey: String = CONSTANTS.API_KEY
    ): List<Image>
}