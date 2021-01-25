package fr.isen.prezut.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.isen.prezut.androiderestaurant.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    //Declaration de mon bouton
    lateinit var boutonEntrees : Button
    lateinit var boutonPlats : Button
    lateinit var boutonDesserts : Button

    fun displayMsg(str: String) {
        Toast.makeText(this, "Bouton cliqué : $str", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //initialisation
        boutonEntrees = findViewById(R.id.entrees_button)
        boutonPlats = findViewById(R.id.plats_button)
        boutonDesserts = findViewById(R.id.dessert_button)

        // creation de notre intent
        val AllIntent : Intent =  Intent(this, RecipeActivity::class.java)


        //clic sur le bouton Entrées
        boutonEntrees.setOnClickListener {
            AllIntent.putExtra("id_button",boutonEntrees.text)
            startActivity(AllIntent)
        }

        //clic sur le bouton Plats
        boutonPlats.setOnClickListener {
            AllIntent.putExtra("id_button",boutonPlats.text)
            startActivity(AllIntent)
        }

        //clic sur le bouton Desserts
        boutonDesserts.setOnClickListener {
            AllIntent.putExtra("id_button",boutonDesserts.text)
            startActivity(AllIntent)
        }
    }


}