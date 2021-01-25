package fr.isen.prezut.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {

    //Declaration de mon bouton
    lateinit var boutonEntrees : Button
    lateinit var boutonPlats : Button
    lateinit var boutonDesserts : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //initialisation
        boutonEntrees = findViewById(R.id.entrees_button)
        boutonPlats = findViewById(R.id.plats_button)
        boutonDesserts = findViewById(R.id.dessert_button)

        // creation de notre intent
        val EntreesIntent : Intent =  Intent(this,EntreesActivity::class.java)

        //clic sur le bouton Entrées
        boutonEntrees.setOnClickListener {
            startActivity(EntreesIntent)
        }

        //clic sur le bouton Plats
        boutonPlats.setOnClickListener {
            startActivity(EntreesIntent)
        }

        //clic sur le bouton Desserts
        boutonDesserts.setOnClickListener {
            startActivity(EntreesIntent)
        }
    }
}