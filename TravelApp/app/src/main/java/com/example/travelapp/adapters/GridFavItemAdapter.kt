package com.example.travelapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.model.FavPresentation
import com.example.travelapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_fav_view.view.*

class GridFavItemAdapter(val favs: ArrayList<FavPresentation>) :
    RecyclerView.Adapter<GridFavItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        if (!favs[position].key.equals("")) {

            val uri = Uri.parse(favs[position].key)
            Picasso.get().load(uri).into(holder.itemView.photoPicture)
        }

        holder.cardFavUsername.text = favs[position].userId
        holder.cardFavPlace.text = "${favs[position].tripId}"
    }

    override fun getItemCount(): Int {

        return favs.size
    }
}