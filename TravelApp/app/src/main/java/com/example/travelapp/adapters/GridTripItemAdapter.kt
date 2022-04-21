package com.example.travelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.model.Trip

class GridTripItemAdapter(val cardCounters: ArrayList<Trip>, val listener: (Trip) -> Unit) :
    RecyclerView.Adapter<GridTripItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardCounter: TextView = itemView.findViewById(R.id.counterLabel)
        val cardCountry: TextView = itemView.findViewById(R.id.countryLabel)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grid_trip_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cardCounter.text = cardCounters[position].favoritesCounter.toString()
        holder.cardCountry.text = cardCounters[position].destinationCountry
        holder.itemView.setOnClickListener {
            listener(cardCounters[position])

        }
    }

    override fun getItemCount(): Int {
        return cardCounters.size
    }


}