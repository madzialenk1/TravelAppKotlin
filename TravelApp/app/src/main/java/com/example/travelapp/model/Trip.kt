package com.example.travelapp.model

import com.example.travelapp.PlaceX
import java.io.Serializable

data class Trip(
    var id: String,
    var travelBuddy: String,
    var destinationCountry: String,
    var endDate: String,
    var startDate: String,
    var favoritesCounter: Long,
    var fav: Boolean,
    var places: ArrayList<PlaceX> = arrayListOf()
) : Serializable
