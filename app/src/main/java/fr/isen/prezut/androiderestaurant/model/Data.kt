package fr.isen.prezut.androiderestaurant.model

import java.io.Serializable

data class Data(
    val name_fr: String,
    val name_en: String,
    val items: List<Item>
) : Serializable