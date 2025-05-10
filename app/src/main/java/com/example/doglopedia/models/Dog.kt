package com.example.doglopedia.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dog(
    val id: Long,
    val name: String,
    val temperament: String = "-",
    val origin: String = "-",

    @SerialName("life_span")
    val lifeSpan: String = "-",

    @SerialName("breed_group")
    val breedGroup: String = "-",

    @SerialName("bred_for")
    val bredFor: String = "-",

    @SerialName("reference_image_id")
    val refImgId: String? = null,

    val weight: Measurement = Measurement(),
    val height: Measurement = Measurement(),

    val description: String = "-"
)

@Serializable
data class Measurement(
    val imperial: String = "-",
    val metric: String = "-"
)