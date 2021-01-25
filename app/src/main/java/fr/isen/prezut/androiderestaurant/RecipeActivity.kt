package fr.isen.prezut.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RecipeActivity : AppCompatActivity() {

    lateinit var myTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val intent = intent

        if (intent != null) {

            var str: String? = ""

            if (intent.hasExtra("id_button")) {
                str = intent.getStringExtra("id_button")
            }

            myTitle = findViewById(R.id.mtitle)
            myTitle.setText(str)
        }
    }
}