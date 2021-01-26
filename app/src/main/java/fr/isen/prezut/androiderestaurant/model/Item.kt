package fr.isen.prezut.androiderestaurant.model

import java.io.Serializable

data class Item(
    val id: Long,
    val name_fr: String,
    val id_category: Long,
    val categ_name_fr: String,
    val images: List<String>,
    val ingredients: List<Ingredient>,
    val prices: List<Price>
) : Serializable