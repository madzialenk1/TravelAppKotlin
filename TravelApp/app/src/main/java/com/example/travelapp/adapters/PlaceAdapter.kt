package com.example.travelapp.adapters

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.travelapp.PlaceX
import com.example.travelapp.R

class PlaceAdapter(private val context: Activity, private val arrayList: ArrayList<PlaceX>) :
    ArrayAdapter<PlaceX>(context, R.layout.trip_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflator: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflator.inflate(R.layout.trip_item, null)

        val imageView: ImageView = view.findViewById(R.id.tripPictureListView)
        val description: TextView = view.findViewById(R.id.descriptionLabel)
        val destination: TextView = view.findViewById(R.id.destinationLabel)

        val uri = Uri.parse(arrayList[position].photo)
        imageView.setImageURI(uri)
        description.text = arrayList[position].description
        destination.text = arrayList[position].name

        return view
    }
}