package com.example.travelapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trip_view_pager.view.*

class ViewPagerTripDetailsAdapter(val trip: Trip, val listener: (Int) -> Unit) :
    RecyclerView.Adapter<ViewPagerTripDetailsAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.trip_view_pager, parent, false)
        return ViewPagerViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curImage = trip.places[position].photo
        val uri = Uri.parse(curImage)
        Picasso.get().load(uri).into(holder.itemView.tripImage);

        holder.itemView.tripImage.outlineProvider = ViewOutlineProvider.BACKGROUND
        holder.itemView.tripImage.clipToOutline = true
        holder.itemView.setOnClickListener {
            listener(position)
        }

    }

    override fun getItemCount(): Int {
        return trip.places.size
    }
}