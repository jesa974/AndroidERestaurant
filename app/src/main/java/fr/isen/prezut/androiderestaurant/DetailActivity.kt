package fr.isen.prezut.androiderestaurant

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    var foodDescription: TextView? = null
    var RecipeName:TextView? = null
    var RecipePrice:TextView? = null
    var foodImage: ImageView? = null
    var key = ""
    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        RecipeName = findViewById<View>(R.id.txtRecipeName) as TextView
        RecipePrice = findViewById<View>(R.id.txtPrice) as TextView
        foodDescription = findViewById<View>(R.id.txtDescription) as TextView
        foodImage = findViewById<View>(R.id.ivImage2) as ImageView

        val mBundle = intent.extras

        if (mBundle != null) {
            foodDescription!!.setText(mBundle.getString("Description"))
            key = mBundle.getString("keyValue").toString()
            imageUrl = mBundle.getString("Image").toString()
            RecipeName!!.setText(mBundle.getString("RecipeName"))
            RecipePrice!!.setText(mBundle.getString("price"))
        }
    }
}