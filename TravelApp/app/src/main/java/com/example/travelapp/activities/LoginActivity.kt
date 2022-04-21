package com.example.travelapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.travelapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        mAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = loginTextField.text.toString()
        val password: String = passwordLoginTextField.text.toString()

        if (email.equals("")) {
            Toast.makeText(this@LoginActivity, "please write email", Toast.LENGTH_LONG).show()

        } else if (password.equals("")) {
            Toast.makeText(this@LoginActivity, "please write password", Toast.LENGTH_LONG).show()

        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserID = mAuth.currentUser!!.uid
                    openHomeScreen()
                    finish()

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error Message: " + task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    public final fun openHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}