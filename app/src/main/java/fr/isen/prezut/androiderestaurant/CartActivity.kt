package fr.isen.prezut.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.isen.prezut.androiderestaurant.adapter.FoodAdaptater
import fr.isen.prezut.androiderestaurant.adapter.CartAdapter
import fr.isen.prezut.androiderestaurant.domain.OrderData
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.StringWriter

class CartActivity :AppCompatActivity() {

    var cart : MutableList<OrderData>? = null
    private lateinit var view: View
    private lateinit var cleanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        readFile()

        val adapter = cart?.let { CartAdapter(it, applicationContext) }
        val OrderList = findViewById<RecyclerView>(R.id.RecyclerViewCart)
        val itemTouchHelper = adapter?.let { DeleteSwipe(it) }?.let { ItemTouchHelper(it) }
        if (itemTouchHelper != null) {
            itemTouchHelper.attachToRecyclerView(OrderList)
        }

        cleanButton = findViewById(R.id.myCleanButton)
        cleanButton.setOnClickListener {
            CleanBasket()
        }
    }

    fun readFile(){
        try {
            val parser = Gson()
            applicationContext.openFileInput(DetailActivity.ORDER_FILE).use {inputStream ->
                inputStream.bufferedReader().use {
                    cart = parser.fromJson(it.readText(), Array<OrderData>::class.java).toMutableList()
                }
        }
            val foodRecycler = findViewById<RecyclerView>(R.id.RecyclerViewCart)
            foodRecycler.adapter = cart?.let { CartAdapter(it, applicationContext) }
            foodRecycler.layoutManager = LinearLayoutManager(this)
            foodRecycler.isVisible = true


        }catch (e : FileNotFoundException){
                Toast.makeText(applicationContext,"No cart found", Toast.LENGTH_LONG).show()
                val title = findViewById<TextView>(R.id.titleCart)
                title.text = "No cart found"
        }
    }


    fun CleanBasket() {
        try {
            applicationContext.deleteFile(DetailActivity.ORDER_FILE)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } catch(e: FileNotFoundException) {
            Toast.makeText(applicationContext, "Cannot retrieve orders", Toast.LENGTH_SHORT).show()
        }
    }
}