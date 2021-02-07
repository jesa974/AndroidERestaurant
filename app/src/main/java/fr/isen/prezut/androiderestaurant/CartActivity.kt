package fr.isen.prezut.androiderestaurant

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSerializer
import com.wajahatkarim3.easyvalidation.core.collection_ktx.creditCardNumberList
import fr.isen.prezut.androiderestaurant.adapter.FoodAdaptater
import fr.isen.prezut.androiderestaurant.adapter.CartAdapter
import fr.isen.prezut.androiderestaurant.domain.OrderData
import fr.isen.prezut.androiderestaurant.domain.OrderResponseData
import fr.isen.prezut.androiderestaurant.domain.RegisterData
import fr.isen.prezut.androiderestaurant.domain.UserData
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.StringWriter

class CartActivity :AppCompatActivity() {

    var cart : MutableList<OrderData>? = null
    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var cleanButton: Button
    private lateinit var commandButton: Button
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
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

        progress = findViewById(R.id.orderProgress)

        commandButton = findViewById(R.id.myCommandButton)
        commandButton.setOnClickListener {
            if (!sharedPref.contains(SignUpActivity.ID_CLIENT)) {
                Toast.makeText(applicationContext, "Not connected. Redirect to sign up", Toast.LENGTH_SHORT).show()
                redirectToSignUp()
            } else {
                Toast.makeText(applicationContext, "Already connected", Toast.LENGTH_SHORT).show()
                FinalOrder()
            }
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
            with(sharedPref.edit()) {
                remove(HomeActivity.ID_QTY)
                apply()
            }
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } catch(e: FileNotFoundException) {
            Toast.makeText(applicationContext, "Cannot retrieve orders", Toast.LENGTH_SHORT).show()
        }
    }

    private fun FinalOrder() {
        // Convert to JsonArray the orders
        val finalOrder = Gson().toJson(cart)
        Log.i(ListActivity.TAG, "The final order is $finalOrder")

        // Save the order to the api
        CommandBasket(finalOrder, sharedPref.getInt(SignUpActivity.ID_CLIENT,-1), finalOrderSavedCallback, progress)
    }

    private val finalOrderSavedCallback = { receiver: String ->
        Toast.makeText(applicationContext, "Cannot retrieve orders", Toast.LENGTH_SHORT).show()
        CleanBasket()
    }

    private fun getQueue(): RequestQueue {
        return Volley.newRequestQueue(applicationContext)
    }

    private fun getParams(): JSONObject {
        val params = JSONObject()
        params.put("id_shop", "1")
        return params
    }

    private fun CommandBasket(jsonOrder: String, userId: Int, onSavedCallback: (receiver: String) -> Unit, progressBar: ProgressBar) {
        val queue = getQueue()
        // params
        val params = getParams()
        params.put("id_user", userId)
        params.put("msg", jsonOrder)

        val req = JsonObjectRequest(
                Request.Method.POST, API_ORDER_URL, params,
                Response.Listener { response ->
                    Log.d(SignInActivity.TAG, "Sent Order Response: $response")
                    // alert the user
                    onSavedCallback(
                            gson.fromJson(
                                    response["data"].toString(),
                                    Array<OrderResponseData>::class.java)[0].receiver
                    )
                },
                Response.ErrorListener { error ->
                    Log.e(ListActivity.TAG, "Error: ${error.message}")
                })
        queue.add(req)
        progressBar.isVisible = true
        queue.addRequestFinishedListener<JsonObjectRequest> {
            // dismiss progress bar
            progressBar.isVisible = false
        }
    }

    private fun redirectToParent() {
        // redirect to dish list
        intent.extras?.getSerializable(SignUpActivity.ITEM)?.let {
            val parent = Intent(this, DetailActivity::class.java)
            // to return to the right activity, the basket activity need the category
            parent.putExtra(SignUpActivity.ITEM, it)
            startActivity(parent)
        } ?: run {
            // by default
            val parent = Intent(this, HomeActivity::class.java)
            startActivity(parent)
        }
    }

    private fun redirectToSignUp() {
        val signUp = Intent(this, SignUpActivity::class.java)
        startActivity(signUp)
    }

    companion object {
        val API_ORDER_URL = "http://test.api.catering.bluecodegames.com/user/order"
    }
}