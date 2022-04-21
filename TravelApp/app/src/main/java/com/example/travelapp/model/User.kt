package com.example.travelapp.model

import java.io.Serializable

data class User(
    var trips: ArrayList<Trip> = arrayListOf(),
    var favorites: ArrayList<FavPresentation> = arrayListOf(),
    var id: String
) : Serializable




