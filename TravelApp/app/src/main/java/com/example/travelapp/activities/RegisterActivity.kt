package com.example.travelapp.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.travelapp.model.Profile
import com.example.travelapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.main_view_pager.view.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    lateinit var imageUri: Uri
    private var firebaseUserID: String = ""

    private val GALLERY_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        mAuth = FirebaseAuth.getInstance()

        uploadImageButton.setOnClickListener {
            galleryCheckPermission()
        }
        uploadPhotoImageView.setOnClickListener {
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItem = arrayOf("Select photo from Gallery")
            pictureDialog.setItems(pictureDialogItem) { dialog, which ->

                when (which) {
                    0 -> gallery()

                }
            }

            pictureDialog.show()
        }

        registerButton.setOnClickListener {

            val email = emailEditField.text.toString()
            val password = passwordEditField.text.toString()
            val hobby = hobbyEditField.text.toString()
            val repeatedPassword = repeatPasswordEditField.text.toString()
            val fullName = fullNameEditField.text.toString()
            val date = calendarImage.date.toString()

            if (email.equals("") || password.equals("") || hobby.equals("") || repeatedPassword.equals(
                    ""
                ) || imageUri.equals(null) || fullName.equals("") || date.equals("")
            ) {

                Toast.makeText(
                    this@RegisterActivity,
                    "please provide all the information",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                registerUser()
                getAllInformation()
            }


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
                    this@RegisterActivity,
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
                    uploadPhotoImageView.setImageURI(imageUri)

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


    private fun getAllInformation() {
        val fullName = fullNameEditField.text.toString()
        val date = calendarImage.date.toString()
        val hobby = hobbyEditField.text.toString()
        val email = emailEditField.text.toString()

        refUsers = FirebaseDatabase.getInstance().getReference("user")
        val User = Profile(fullName, hobby, date, email)
        var uniqueID = UUID.randomUUID().toString()
        uploadImage(uniqueID)
        refUsers.child(uniqueID).child("profile").setValue(User).addOnSuccessListener {
            fullNameEditField.text.clear()
            hobbyEditField.text.clear()

        }
    }

    private fun uploadImage(uid: String) {

        val storageRef =
            FirebaseStorage.getInstance().getReference("images/$uid/profile/profilePhoto")

        storageRef.putFile(imageUri).addOnSuccessListener {

        }.addOnFailureListener {


        }
    }

    private fun registerUser() {
        val email: String = emailEditField.text.toString()
        val password: String = passwordEditField.text.toString()
        val repeatedPassword = repeatPasswordEditField.text.toString()


        if (email.equals("")) {
            Toast.makeText(this@RegisterActivity, "please write email", Toast.LENGTH_LONG).show()

        } else if (password.equals("") || !password.equals(repeatedPassword)) {
            Toast.makeText(this@RegisterActivity, "please write password", Toast.LENGTH_LONG).show()

        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserID = mAuth.currentUser!!.uid
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error Message: " + task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

        }
    }
}