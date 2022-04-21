package com.example.travelapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelapp.PlaceX
import com.example.travelapp.R
import com.example.travelapp.activities.TripDetailsActivity
import com.example.travelapp.adapters.GridTripItemAdapter
import com.example.travelapp.model.Trip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_trip.*
import java.io.Serializable

class TripFragment : Fragment(), Serializable {
    companion object {
        val INTENT_PARCELABLE = "0bject intent"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTrips {
            val adapter = GridTripItemAdapter(it, listener = {
                val intent = Intent(context, TripDetailsActivity::class.java)
                intent.putExtra(INTENT_PARCELABLE, it)
                startActivity(intent)
            })
            val gridLayout = GridLayoutManager(context, 1)
            recycleViewTrips.layoutManager = gridLayout
            recycleViewTrips.adapter = adapter

        }
    }


    private fun getTrips(callback: (ArrayList<Trip>) -> Unit) {

        val user = Firebase.auth.currentUser
        if (user != null) {
            val id = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("user")
            ref.child(id).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val trips: ArrayList<Trip> = arrayListOf()

                        for (trip in it.child("trips").children) {
                            val destinationCountry =
                                trip.child("destinationCountry").value as String
                            val endDate = trip.child("endDate").value as String
                            val favoritesCounter = trip.child("favouritesCounter").value as Long
                            val startDate = trip.child("startDate").value as String
                            val idTrip = trip.child("id").value as String

                            val allTrip = Trip(
                                idTrip,
                                "no information",
                                destinationCountry,
                                endDate,
                                startDate,
                                favoritesCounter,
                                false
                            )

                            for (place in trip.child("places").children) {
                                val description = place.child("description").value as String
                                val destination = place.child("destination").value as String
                                getImage(id, idTrip, destination, listener = {
                                    val placeX = PlaceX(destination, it.toString(), description)
                                    allTrip.places.add(placeX)
                                })

                            }

                            trips.add(allTrip)

                        }
                        callback(trips)
                    }
                }

        } else {
            println("No user is signed")
        }


    }

    private fun getImage(
        uid: String,
        tripId: String,
        destination: String,
        listener: (Uri) -> Unit
    ) {

        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/$uid/places/$tripId/$destination.jpeg")
        storageRef.downloadUrl.addOnSuccessListener {
            listener(it)
        }.addOnFailureListener {

        }

    }
}