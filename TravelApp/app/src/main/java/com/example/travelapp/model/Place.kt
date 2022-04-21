package com.example.travelapp

import android.net.Uri
import java.io.Serializable

data class PlaceX(var name: String, var photo: String = "", var description: String) : Serializable
