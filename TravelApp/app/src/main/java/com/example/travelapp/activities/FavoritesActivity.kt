package com.example.travelapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelapp.*
import com.example.travelapp.adapters.GridFavItemAdapter
import com.example.travelapp.fragments.HomeFragment
import com.example.travelapp.model.FavPresentation
import com.example.travelapp.model.Favorites
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val favs = intent.getSerializableExtra(HomeFragment.FAV) as ArrayList<FavPresentation>
        val adapter = GridFavItemAdapter(favs)
        val gridLayout = GridLayoutManager(this, 2)
        recycleViewFavs.layoutManager = gridLayout
        recycleViewFavs.adapter = adapter


    }

    private fun getFavs(callback: (ArrayList<Favorites>) -> Unit) {

        val user = Firebase.auth.currentUser
        if (user != null) {
            val id = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("user")
            ref.child(id).get()
                .addOnSuccessListener {
                    if (it.exists()) {

                        val favs = ArrayList<Favorites>()
                        for (fav in it.child("favourites").children) {

                            val tripId = fav.child("tripId").value as String
                            val userId = fav.child("userId").value as String
                            val fav = Favorites(tripId, userId)
                            favs.add(fav)

                        }
                        callback(favs)
                    }
                }

        } else {
            println("No user is signed")
        }
    }
}