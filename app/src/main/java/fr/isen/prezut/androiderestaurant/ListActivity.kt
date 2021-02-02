package fr.isen.prezut.androiderestaurant


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.prezut.androiderestaurant.SignUpActivity.Companion.ID_CLIENT
import fr.isen.prezut.androiderestaurant.adapter.FoodAdaptater
import fr.isen.prezut.androiderestaurant.domain.ResponseData
import org.json.JSONObject


class ListActivity : AppCompatActivity(){

    private var title:TextView? = null
    private val API_URL = "http://test.api.catering.bluecodegames.com/menu"
    private var CAT:Int = 0
    private lateinit var dataRecycler:ResponseData
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
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
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val bundle = intent.extras
        title = findViewById<View>(R.id.title) as TextView

        if (bundle != null) {
            title!!.text = bundle.getString("title")
            this.CAT = bundle.getInt("cat")
        }

        volleyGet()

        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    private fun volleyGet() {

        val parameter = JSONObject()
        parameter.put("id_shop", "1")
        val requestQueue = Volley.newRequestQueue(this)
        val parser = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.POST, API_URL, parameter, Response.Listener<JSONObject> { response ->
                val result = parser.fromJson(response["data"].toString(), Array<ResponseData>::class.java)
                this.dataRecycler = result[this.CAT];
               //System.out.println(this.CAT)

                val foodRecycler = findViewById<RecyclerView>(R.id.RecyclerView)
                foodRecycler.adapter = FoodAdaptater(dataRecycler.items, applicationContext)
                foodRecycler.layoutManager = LinearLayoutManager(this)

                foodRecycler.isVisible = true
            },
                Response.ErrorListener { error -> System.err.println( "Error: ${error.message}")
                    Toast.makeText(applicationContext,"Error: ${error.message}",Toast.LENGTH_LONG ).show()
                })

        requestQueue.add(jsonObjectRequest)

    }

    companion object {
        val TAG: String = ListActivity::class.java.simpleName
    }

}

