package com.example.travelapp.adapters


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.example.travelapp.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_fav_view.view.*

class GridMainItemAdapter(
    val currentUserId: String?,
    val users: ArrayList<User>,
    val listener: (String, String, String) -> Unit
) : RecyclerView.Adapter<GridMainItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardFavImage: ImageView = itemView.findViewById(R.id.photoPicture)
        val cardFavPlace: TextView = itemView.findViewById(R.id.favPlace)
        val cardFavUsername: TextView = itemView.findViewById(R.id.favUsername)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grid_fav_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val trips: ArrayList<Trip> = arrayListOf()
        for (user in users) {
            for (trip in user.trips) {
                println(trip.fav)
                trips.add(trip)
            }
        }

        if (!trips[position].places.isEmpty()) {
            val curImage = trips[position].places.get(0).photo

            val uri = Uri.parse(curImage)
            Picasso.get().load(uri).into(holder.itemView.photoPicture)
        }

        if (trips[position].fav == true) {
            holder.itemView.favButton.setTag("full_heart")
            holder.itemView.favButton.setImageResource(R.drawable.full_heart)
        } else {
            holder.itemView.favButton.setTag("empty_heart")
            holder.itemView.favButton.setImageResource(R.drawable.empty_heart)
        }

        holder.cardFavUsername.text = trips[position].destinationCountry
        holder.cardFavPlace.text = "${trips[position].startDate}-${trips[position].endDate}"

        holder.itemView.favButton.setOnClickListener {
            if (holder.itemView.favButton.tag == "full_heart") {
                holder.itemView.favButton.setTag("empty_heart")
                holder.itemView.favButton.setImageResource(R.drawable.empty_heart)
                listener(trips[position].id, trips[position].destinationCountry, "remove")
            } else {
                holder.itemView.favButton.setTag("full_heart")
                holder.itemView.favButton.setImageResource(R.drawable.full_heart)
                listener(trips[position].id, trips[position].destinationCountry, "add")
            }
        }
    }

    override fun getItemCount(): Int {
        var counter: Int = 0
        for (user in users) {
            for (trip in user.trips) {
                counter += 1
            }
        }
        return counter
    }
}