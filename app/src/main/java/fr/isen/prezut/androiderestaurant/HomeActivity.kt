package fr.isen.prezut.androiderestaurant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.gson.GsonBuilder
import fr.isen.prezut.androiderestaurant.SignInActivity.Companion.FIRST_TIME_SIGN_IN
import fr.isen.prezut.androiderestaurant.SignUpActivity.Companion.ID_CLIENT
import fr.isen.prezut.androiderestaurant.domain.FoodData
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    var myFoodList: List<FoodData>? = null
    private lateinit var sharedPref: SharedPreferences
    private lateinit var toolbarMenu: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        setFirstTimeSignIn(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        //Toast.makeText(this.applicationContext, "Destroy main", Toast.LENGTH_SHORT).show()
    }

    private fun setFirstTimeSignIn(value: Boolean) {
        if (!sharedPref.contains(FIRST_TIME_SIGN_IN)) {
            with(sharedPref.edit()) {
                putBoolean(FIRST_TIME_SIGN_IN, value)
                apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        /*val shared = this.getPreferences(Context.MODE_PRIVATE)
        val nb = shared.getInt("qtCart", 0)
        countPastille(nb)*/

        return super.onCreateOptionsMenu(menu);
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.actionLogOut)?.let {
            if (!sharedPref.contains(ID_CLIENT)) {
                it.setTitle(R.string.action_log_in)
            } else {
                it.setTitle(R.string.action_log_out)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item -> {
                startActivity(
                    Intent(applicationContext, CartActivity::class.java)
                )
                true
            }
            R.id.actionLogOut -> {
                if (sharedPref.contains(ID_CLIENT)) {
                    with(sharedPref.edit()) {
                        remove(ID_CLIENT)
                        apply()
                    }
                    Toast.makeText(this.applicationContext, "Log Out successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun btnMenu(view: View?) {
        if (R.id.entree == view?.id){
            //Toast.makeText(this.applicationContext, "Entrées", Toast.LENGTH_SHORT).show()
            changePage("Entrées",0)
        }else if (R.id.plat == view?.id){
            //Toast.makeText(this.applicationContext, "Plats", Toast.LENGTH_SHORT).show()
            changePage("Plat",1)
        }else if (R.id.dessert == view?.id){
            //Toast.makeText(this.applicationContext, "Dessert", Toast.LENGTH_SHORT).show()
            changePage("Dessert",2)
        }
    }


    fun changePage(string: String, category :Int) {
        startActivity(
            Intent(applicationContext, ListActivity::class.java)
                .putExtra("title",string )
                .putExtra("cat", category)
        )
    }

    private fun countPastille(num :Int){
        val countText = findViewById<TextView>(R.id.numberCart)
        countText.text = num.toString()
    }


}