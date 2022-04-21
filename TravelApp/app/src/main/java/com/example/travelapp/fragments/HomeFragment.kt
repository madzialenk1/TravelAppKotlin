package com.example.travelapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelapp.PlaceX
import com.example.travelapp.R
import com.example.travelapp.activities.FavoritesActivity
import com.example.travelapp.adapters.GridMainItemAdapter
import com.example.travelapp.model.FavPresentation
import com.example.travelapp.model.Favorites
import com.example.travelapp.model.Trip
import com.example.travelapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    var users = ArrayList<User>()

    companion object {
        val FAV = "fav object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        readData {


            val user = Firebase.auth.currentUser
            if (user != null) {
                if (!it.isEmpty()) {
                    val user = it.filter { x -> x.id == user.uid }
                    var favs = ArrayList<FavPresentation>()
                    if (!user.isEmpty()) {
                        for (i in user[0].favorites) {
                            for (user in users) {
                                var trip =
                                    user.trips.filter { trip -> trip.id == i.tripId }
                                if (!trip.isEmpty()) {
                                    val destination = trip[0].destinationCountry
                                    val startDate = trip[0].startDate
                                    trip[0].fav = true
                                    var picture: String
                                    if (trip[0].places.isEmpty()) {
                                        picture = ""
                                    } else {
                                        picture = trip[0].places[0].photo
                                    }
                                    val favorite =
                                        FavPresentation(destination, startDate, picture)
                                    favs.add(favorite)


                                }
                            }
                        }
                        if (!favs.isEmpty()) {
                            user[0].favorites = favs
                        }
                    }
                }
            }


            val adapter = GridMainItemAdapter(user?.uid, it, listener = { it, country, method ->
                var userId: String = ""
                for (user in users) {
                    for (trip in user.trips) {
                        if (trip.id == it) {
                            userId = user.id
                            break
                        }
                    }
                }
                if (method == "add") {
                    sendFavoritesToDatabase(userId, country, it)
                } else {
                    removeFromDatabase(userId, country, it)

                }
            })
            val gridLayout = GridLayoutManager(context, 2)

            recycleViewHomeScreen.layoutManager = gridLayout
            recycleViewHomeScreen.adapter = adapter

        }


        box2.setOnClickListener {
            val intent = Intent(context, FavoritesActivity::class.java)
            val user = users.firstOrNull { it.id == Firebase.auth.currentUser?.uid }

            intent.putExtra(FAV, user?.favorites)
            startActivity(intent)

        }
    }

    private fun sendFavoritesToDatabase(userId: String, country: String, tripId: String) {

        val user = Firebase.auth.currentUser
        if (user != null) {
            val id = user.uid
            val fav = Favorites(userId, tripId)
            val ref = FirebaseDatabase.getInstance().getReference("user")
            var uniqueID = UUID.randomUUID().toString()
            ref.child(id).child("favourites").child(uniqueID).setValue(fav).addOnSuccessListener {

            }
            val refX = FirebaseDatabase.getInstance().getReference().child("user")
                .child(userId).child("trips").child(country)

            refX.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val favCounter =
                        snapshot.child("favouritesCounter").value as Long
                    snapshot.ref.child("favouritesCounter").setValue(favCounter + 1)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun removeFromDatabase(userId: String, country: String, tripId: String) {

        val user = Firebase.auth.currentUser
        if (user != null) {
            val id = user.uid
            val ref = FirebaseDatabase.getInstance().getReference().child("user").child(id)
                .child("favourites")
            ref.orderByChild("tripId").equalTo(tripId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (post in dataSnapshot.children) {
                            val id = post.key as String
                            ref.child(id).removeValue()
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

            val refX = FirebaseDatabase.getInstance().getReference().child("user")
                .child(userId).child("trips").child(country)

            refX.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val favCounter =
                        snapshot.child("favouritesCounter").value as Long
                    snapshot.ref.child("favouritesCounter").setValue(favCounter - 1)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


    private fun readData(callback: (ArrayList<User>) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.get()
            .addOnSuccessListener {
                if (it.exists()) {


                    for (person in it.children) {
                        val trips = ArrayList<Trip>()
                        val key = person.key

                        for (trip in person.child("trips").children) {
                            val destinationCountry =
                                trip.child("destinationCountry").value as String
                            val endDate = trip.child("endDate").value as String
                            val favoritesCounter = trip.child("favouritesCounter").value as Long
                            val id = trip.child("id").value as String
                            val startDate = trip.child("startDate").value as String

                            val allTrip = Trip(
                                id,
                                "No information",
                                destinationCountry,
                                endDate,
                                startDate,
                                favoritesCounter,
                                false

                            )

                            for (place in trip.child("places").children) {
                                val description = place.child("description").value as String
                                val destination = place.child("destination").value as String
                                getImage(key as String, id, destination, listener = {
                                    val placeX = PlaceX(destination, it.toString(), description)
                                    allTrip.places.add(placeX)
                                    callback(users)

                                })
                            }

                            trips.add(allTrip)

                        }

                        var favs: ArrayList<FavPresentation> = arrayListOf()
                        if (person.hasChild("favourites")) {
                            for (fav in person.child("favourites").children) {
                                val key = fav.key
                                val userId = fav.child("userId").value as String
                                val tripId = fav.child("tripId").value as String

                                val favorite = FavPresentation(userId, tripId, key!!)
                                favs.add(favorite)
                            }
                        }
                        var user = User(trips, favs, key!!)
                        users.add(user)

                    }

                    callback(users)


                } else {
                    Toast.makeText(context, "User doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {

                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun getImage(
        uid: String,
        tripId: String,
        destination: String,
        listener: (Uri) -> Unit
    ) {
        println("images/$uid/places/$tripId/$destination.jpeg")
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/$uid/places/$tripId/$destination.jpeg")
        storageRef.downloadUrl.addOnSuccessListener {
            listener(it)
        }.addOnFailureListener {

        }

    }

}