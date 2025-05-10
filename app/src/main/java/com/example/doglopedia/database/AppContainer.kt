package com.example.doglopedia.database

import com.example.doglopedia.network.DogApiService
import com.example.doglopedia.utils.CONSTANTS
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val dogsRepository: DogsRepository
}

class DefaultAppContainer : AppContainer {

    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val doglopediaJson = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(doglopediaJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(CONSTANTS.BASE_URL)
        .build()

    private val retrofitService: DogApiService by lazy {
        retrofit.create(DogApiService::class.java)
    }

    override val dogsRepository: DogsRepository by lazy {
        NetworkMoviesRepository(retrofitService)
    }
}