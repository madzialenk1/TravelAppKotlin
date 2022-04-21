package com.example.travelapp.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.travelapp.adapters.PlaceAdapter
import com.example.travelapp.PlaceX
import com.example.travelapp.R
import com.example.travelapp.model.Trip
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_new_trip_form.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_trip_planner.*
import kotlinx.android.synthetic.main.activity_trip_planner.view.*
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class NewTripFormActivity : AppCompatActivity() {

    private val GALLERY_REQUEST_CODE = 2
    lateinit var imageUri: Uri
    lateinit var mDialogView: View
    var placesArray = ArrayList<PlaceX>()
    var endDate: String = ""
    var startDate: String = ""
    private lateinit var refUsers: DatabaseReference
    var placeX: String = ""
    var travelBuddy = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip_form)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key))
        }

        val today = Calendar.getInstance()
        datePickerEndDate.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            endDate = "$day/$month/$year"
        }


        plusButton.setOnClickListener {

            mDialogView = LayoutInflater.from(this).inflate(R.layout.activity_trip_planner, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()


            val autocompleteSupportFragment1 =
                supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

            autocompleteSupportFragment1!!.setPlaceFields(
                Arrays.asList(Place.Field.ID, Place.Field.NAME)
            )
            autocompleteSupportFragment1.setOnPlaceSelectedListener(object :
                PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    placeX = place.name

                }

                override fun onError(p0: Status) {
                    TODO("Not yet implemented")
                }

            })


            mDialogView.placePicture.setOnClickListener {
                galleryCheckPermission()
            }

            mDialogView.addButton.setOnClickListener {
                val destination = placeX
                val description = mDialogView.editDescriptionPlace.text.toString()

                if (!destination.equals("") && !description.equals("") && !imageUri.equals(null)) {
                    mAlertDialog.dismiss()

                    this.supportFragmentManager?.also { fragmentManager ->
                        fragmentManager.findFragmentById(R.id.autocomplete_fragment)
                            ?.also { fragment ->
                                fragmentManager.beginTransaction().remove(fragment).commit()
                            }
                    }

                    val imageUri = imageUri
                    val name = destination
                    val descriptions = description


                    val place = PlaceX(name, imageUri.toString(), descriptions)
                    placesArray.add(place)

                    if (placesArray.size != 0) {
                        listViewTrips.background = null
                    }
                    listViewTrips.adapter = PlaceAdapter(this, placesArray)
                } else {
                    Toast.makeText(
                        this@NewTripFormActivity,
                        "please provide all the information",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        submitButton.setOnClickListener {
            var uniqueID = UUID.randomUUID().toString()
            val destination = tripPlaceLabel.text.toString()
            val endDate = endDate
            val startDate = startDate
            val trip =
                Trip(uniqueID, travelBuddy, destination, endDate, startDate, 0, false, placesArray)
            refUsers = FirebaseDatabase.getInstance().getReference("user")
            val user = Firebase.auth.currentUser
            if (user != null && !destination.equals("") && !endDate.equals("") && !startDate.equals(
                    ""
                ) && !placesArray.isEmpty()
            ) {
                val id = user.uid

                refUsers.child(id).child("trips").child(destination).setValue(trip)
                    .addOnSuccessListener {
                    }
                for (i in placesArray) {

                    val uri = Uri.parse(i.photo)
                    uploadImage(id, i.name, uri, uniqueID)

                }

            } else {
                Toast.makeText(
                    this@NewTripFormActivity,
                    "please provide all the information",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun onClickTravelButton(view: View) {
        when (view.id) {
            R.id.buttonYes -> {
                travelBuddy = buttonYes.text.toString()
                buttonYes.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.pink
                    )
                )
                buttonNo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
            }
            R.id.buttonNo -> {
                travelBuddy = buttonNo.text.toString()
                buttonNo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.pink
                    )
                )
                buttonYes.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
            }
        }


    }

    private fun uploadImage(uid: String, destination: String, imageUri: Uri, uniqueId: String) {

        val storageRef =
            FirebaseStorage.getInstance().getReference("images/$uid/places/$uniqueId/$destination")

        storageRef.putFile(imageUri).addOnSuccessListener {

        }.addOnFailureListener {


        }

    }


    private fun galleryCheckPermission() {

        Dexter.withActivity(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@NewTripFormActivity,
                    "You have denied the storage permission to select image",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                GALLERY_REQUEST_CODE -> {

                    imageUri = data?.data!!
                    mDialogView.placePicture.setImageURI(imageUri)

                }
            }

        }

    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off permissions"
                        + "required for this feature. It can be enable under App settings!!!"
            )

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}