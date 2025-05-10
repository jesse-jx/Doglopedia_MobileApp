package com.example.doglopedia.database

import com.example.doglopedia.models.Dog
import com.example.doglopedia.models.Image
import com.example.doglopedia.network.DogApiService

interface DogsRepository {
    suspend fun getDogs(): List<Dog>
    suspend fun getDogImagesById(id: Long, limit: Int = 100): List<Image>
}

class NetworkMoviesRepository(private val apiService: DogApiService) : DogsRepository {
    override suspend fun getDogs() : List<Dog> {
        return apiService.getDogs()
    }
    override suspend fun getDogImagesById(id: Long, limit: Int): List<Image> {
        return apiService.getDogImagesById(id, limit)
    }
}
