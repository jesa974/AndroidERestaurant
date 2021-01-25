package fr.isen.prezut.androiderestaurant

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*

class RecipeActivity : AppCompatActivity() {

    lateinit var myTitle : TextView

    var mRecyclerView: RecyclerView? = null
    var myFoodList: List<FoodData>? = null
    var mFoodData: FoodData? = null
    var myAdapter: MyAdapter? = null
    var txt_Search: EditText? = null

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



        mRecyclerView = findViewById<View>(R.id.my_recycler) as RecyclerView
        val gridLayoutManager = GridLayoutManager(this@RecipeActivity, 1)
        mRecyclerView!!.layoutManager = gridLayoutManager

        txt_Search = findViewById<View>(R.id.txt_searchtext) as EditText

        myFoodList = ArrayList()

        myAdapter = MyAdapter(this@RecipeActivity, myFoodList)
        mRecyclerView!!.adapter = myAdapter

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Recipe")

        val eventListener: ValueEventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (myFoodList as ArrayList<FoodData>).clear()
                for (itemSnapshot in dataSnapshot.getChildren()) {
                    val foodData: FoodData? = itemSnapshot.getValue(FoodData::class.java)
                    if (foodData != null) {
                        foodData.setKey(itemSnapshot.getKey())
                    }
                    if (foodData != null) {
                        (myFoodList as ArrayList<FoodData>).add(foodData)
                    }
                }
                myAdapter!!.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        txt_Search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })


    }

    private fun filter(text: String) {
        val filterList = ArrayList<FoodData>()
        for (item in myFoodList!!) {
            if (item.getItemName()?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                filterList.add(item)
            }
        }
        myAdapter!!.filteredList(filterList)
    }
}