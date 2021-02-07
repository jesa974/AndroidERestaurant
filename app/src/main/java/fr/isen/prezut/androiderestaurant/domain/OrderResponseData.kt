package fr.isen.prezut.androiderestaurant.domain

import java.io.Serializable

data class OrderResponseData(
        val id_receiver: Int,
        val receiver: String
) : Serializable