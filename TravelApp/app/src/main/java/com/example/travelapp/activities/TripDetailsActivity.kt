package com.example.travelapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.example.travelapp.fragments.TripFragment
import com.example.travelapp.adapters.ViewPagerTripDetailsAdapter
import kotlinx.android.synthetic.main.activity_trip_details.*

class TripDetailsActivity : AppCompatActivity() {

    companion object {
        val INTENT_OBJECT = "intent objective"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        val trip = intent.getSerializableExtra(TripFragment.INTENT_PARCELABLE) as Trip
        tripDatesLabel.text = "${trip.startDate} - ${trip.endDate}"
        tripCountryLabel.text = trip.destinationCountry

        val adaptor = ViewPagerTripDetailsAdapter(trip, listener = {

            val intent = Intent(this, PlaceDetailsActivity::class.java)
            intent.putExtra(INTENT_OBJECT, trip.places[it])
            startActivity(intent)

        })
        viewPagerTripDetails.adapter = adaptor


    }
}