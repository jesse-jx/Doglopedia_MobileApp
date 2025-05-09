package com.example.doglopedia.models

data class DogBreeds(
    val id: Int,
    val name: String,
    val image: Image?
)

data class Image(
    val url: String
)