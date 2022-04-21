package com.example.travelapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.travelapp.R
import com.example.travelapp.adapters.TabPageAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        setUpTabBar()
    }

    private fun setUpTabBar() {
        val adapter = TabPageAdapter(this, tabLayout.tabCount)
        tabViewPager.adapter = adapter
        tabViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                tabViewPager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    public final fun openFavs(view: View) {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }

    public final fun openProfile(view: View) {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }

    public final fun openTripRegistration(view: View) {
        val intent = Intent(this, NewTripFormActivity::class.java)
        startActivity(intent)
    }
}