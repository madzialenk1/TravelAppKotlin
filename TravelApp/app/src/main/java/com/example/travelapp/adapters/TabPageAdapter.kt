package com.example.travelapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.travelapp.fragments.HomeFragment
import com.example.travelapp.fragments.TripFragment

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> TripFragment()
            else -> HomeFragment()
        }
    }
}