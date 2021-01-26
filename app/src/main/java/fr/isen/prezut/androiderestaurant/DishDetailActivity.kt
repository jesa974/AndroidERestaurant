package fr.isen.prezut.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.prezut.androiderestaurant.databinding.ActivityDishDetailBinding
import fr.isen.prezut.androiderestaurant.model.Item

class DishDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDishDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val dish = intent.getSerializableExtra("dish") as? Item

        binding.dishName.text = dish?.name_fr
        binding.myDescription.text = dish?.ingredients?.map { it.name_fr }?.joinToString { ", " }
        binding.dishPrice.text = dish?.prices?.get(0)?.price.toString()

    }
}