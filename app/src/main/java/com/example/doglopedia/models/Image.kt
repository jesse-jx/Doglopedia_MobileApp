package com.example.doglopedia.models

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val id: String,
    val url: String
)