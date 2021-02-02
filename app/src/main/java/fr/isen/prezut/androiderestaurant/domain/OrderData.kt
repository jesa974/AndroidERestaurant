package fr.isen.prezut.androiderestaurant.domain

import java.io.Serializable

data class OrderData(
    val item : FoodData,
    var idOrder : Int,
    var quantity : Int,
    var price : Double


):Serializable