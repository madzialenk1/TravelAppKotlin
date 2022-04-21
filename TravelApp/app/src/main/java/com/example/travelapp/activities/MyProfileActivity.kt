package com.example.travelapp.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.travelapp.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.File

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val user = Firebase.auth.currentUser

        if (user != null) {
            readData(user.uid)
            getImage(user.uid)
        }
    }

    private fun readData(uid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(uid).child("profile").get().addOnSuccessListener {
            if (it.exists()) {

                val hobby = it.child("hobby").value
                val fullName = it.child("fullName").value
                val dateOfBirth = it.child("dateOfBirth").value
                val email = it.child("email").value

                fullNameLabel.text = fullName.toString()
                hobbyLabel.text = hobby.toString()
                emailLabel.text = email.toString()


            } else {
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getImage(uid: String) {

        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/$uid/profile/profilePhoto.jpeg")
        val localfile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            pictureProfileScreen.setImageBitmap(bitmap)

        }.addOnFailureListener {

        }

    }
}