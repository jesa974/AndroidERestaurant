package fr.isen.prezut.androiderestaurant.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.isen.prezut.androiderestaurant.DetailActivity
import fr.isen.prezut.androiderestaurant.R
import fr.isen.prezut.androiderestaurant.domain.OrderData
import java.io.BufferedReader
import java.io.FileNotFoundException


class CartAdapter(
        val data: MutableList<OrderData>,
        val context: Context
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private lateinit var view: View
    private lateinit var recentlyDeletedOrder: OrderData
    private var recentlyDeletedOrderPosition: Int = -1


    inner class ViewHolder(dataView: View) : RecyclerView.ViewHolder(dataView) {
        val text: TextView = dataView.findViewById(R.id.tvTitle)
        val desc: TextView = dataView.findViewById(R.id.tvDescription)
        val price: TextView = dataView.findViewById(R.id.tvPrice)
        val img: ImageView = dataView.findViewById(R.id.ivImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.data_layout, parent, false)

        return ViewHolder(contactView)
    }


    override fun onBindViewHolder(viewHolder: CartAdapter.ViewHolder, position: Int) {

        val data: OrderData = data[position]

        val desc = viewHolder.desc
        desc.text = data.quantity.toString()

        val price = viewHolder.price
        val pr: java.lang.StringBuilder = StringBuilder((data.price * data.quantity).toString())
        pr.append(" €")
        price.text = pr

        val textView = viewHolder.text
        textView.text = data.item.name_fr

        val img = viewHolder.img
        val picasso = Picasso.get()
        if (data.item.images[0].isNotEmpty()) {
            picasso.load(data.item.images[0]).into(img)
        } else {
            picasso.load(R.drawable.lasagnes).into(img)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    private fun retrieveOrdersFromReader(reader: BufferedReader, gson: Gson): MutableList<OrderData> {
        val ordersJsonString = reader.readText()
        return gson.fromJson(ordersJsonString, Array<OrderData>::class.java).toMutableList()
    }

    private fun deleteOrder(position: Int) {
        try {
            view.context.openFileInput(DetailActivity.ORDER_FILE).use { inputStream ->
                inputStream.bufferedReader().use {
                    val gson = Gson()
                    val ordersFromFile = retrieveOrdersFromReader(it, gson)
                    // delete the order
                    ordersFromFile.removeAt(position)
                    val ordersToFile = gson.toJson(ordersFromFile)
                    // save the file again
                    view.context.applicationContext.openFileOutput(DetailActivity.ORDER_FILE, Context.MODE_PRIVATE).use { outputStream ->
                        outputStream.write(ordersToFile.toString().toByteArray())
                        Log.i(DetailActivity.TAG, "deleted order from basket: $ordersToFile")
                    }
                    // Update shared preferences
                    val sharedPref = context.getSharedPreferences(
                            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    val currentQuantity = sharedPref.getInt(DetailActivity.QUANTITY_KEY, 0)
                    // add the quantity to the previous quantity
                    with(sharedPref.edit()) {
                        putInt(DetailActivity.QUANTITY_KEY, currentQuantity - recentlyDeletedOrder.quantity)
                        apply()
                    }
                }
            }
        } catch(e: FileNotFoundException) {
            // Alert the user that there are no orders yet
            Toast.makeText(context, "Cannot retrieve orders", Toast.LENGTH_SHORT).show()
        }
    }

    private fun undoDelete() {
        data.add(recentlyDeletedOrderPosition, recentlyDeletedOrder)
        notifyItemInserted(recentlyDeletedOrderPosition)
    }

    private fun showUndoSnackbar() {
        val snackbar = Snackbar.make(view, R.string.deleted_order, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.deleted_order) {
            undoDelete()
        }
        snackbar.show()
    }

    fun deleteItem(position: Int) {
        recentlyDeletedOrder = data[position]
        recentlyDeletedOrderPosition = position
        data.removeAt(position)
        // remove it in the file
        deleteOrder(position)
        // notify the recycler view
        notifyItemRemoved(position)
        showUndoSnackbar()
    }
}

