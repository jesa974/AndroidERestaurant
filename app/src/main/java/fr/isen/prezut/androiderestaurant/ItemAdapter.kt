package fr.isen.prezut.androiderestaurant

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.prezut.androiderestaurant.databinding.ActivityHomeBinding
import fr.isen.prezut.androiderestaurant.model.Item

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class ItemAdapter(
    private val items: List<Item>,
    private val context: Context
) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    inner class ItemHolder(val layout: View) : RecyclerView.ViewHolder(layout) {

        val dishNameView: TextView = itemView.findViewById(R.id.dishName)
        val dishImage: ImageView = itemView.findViewById(R.id.dishImage)
        val dishPrice: TextView = itemView.findViewById(R.id.dishPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.layout_dish_card, parent, false)
        return ItemHolder(contactView)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemHolder, position: Int) {
        val item: Item = items[position]

        // Set item views based on your views and data model
        val textView = holder.dishNameView
        textView.text = item.name_fr

        // Set image if it exists
        val picasso = Picasso.get()
        if (item.images.size > 1) {
            picasso
                .load(item.images[0])
                .resize(400, 400)
                .into(holder.dishImage)
        } else {
            picasso
                .load(R.drawable.lasagnes)
                .resize(400, 400)
                .into(holder.dishImage)
        }

        // Price
        if (item.prices.isNotEmpty()) {
            holder.dishPrice.text = item.prices[0].price.toString()
        }

        // listener on the item
        holder.layout.setOnClickListener {
            // intent with external context
            val intent = Intent(context, DishDetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK


            intent.putExtra("dish", item)
            //intent.putExtra(ITEM, item.id)
            context.startActivity(intent)
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    companion object {

    }
}