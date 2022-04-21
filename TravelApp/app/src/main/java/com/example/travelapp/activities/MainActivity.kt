package com.example.travelapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.travelapp.R
import com.example.travelapp.adapters.ViewPagerMainScreenAdaptor
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {

        lateinit var appContext: Context

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appContext = applicationContext
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val images = listOf(
            R.drawable.travel_buddy,
            R.drawable.travel_buddy_2, R.drawable.travel_buddy_3
        )
        val adaptor = ViewPagerMainScreenAdaptor(images)
        viewPager.adapter = adaptor
        indicator.setViewPager(viewPager)
        adaptor.registerAdapterDataObserver(indicator.adapterDataObserver)

        FirebaseApp.initializeApp(appContext)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    public final fun openLoginPage(view: View) {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    public final fun openRegisterPage(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}