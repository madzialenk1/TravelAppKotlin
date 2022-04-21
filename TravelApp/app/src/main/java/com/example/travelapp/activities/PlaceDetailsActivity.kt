package com.example.travelapp.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.travelapp.PlaceX
import com.example.travelapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_details.*

class PlaceDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val place = intent.getSerializableExtra(TripDetailsActivity.INTENT_OBJECT) as PlaceX
        placeDescriptionLabel.text = place.description
        placeDestinationLabel.text = place.name
        val uri = Uri.parse(place.photo)
        Picasso.get().load(uri).into(placeImage)

    }
}