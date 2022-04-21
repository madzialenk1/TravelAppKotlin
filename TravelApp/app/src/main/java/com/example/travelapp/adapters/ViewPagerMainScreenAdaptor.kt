package com.example.travelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import kotlinx.android.synthetic.main.main_view_pager.view.*

class ViewPagerMainScreenAdaptor(val images: List<Int>) :
    RecyclerView.Adapter<ViewPagerMainScreenAdaptor.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_view_pager, parent, false)
        return ViewPagerViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curImage = images[position]
        holder.itemView.findViewById<ImageView>(R.id.image).setImageResource(curImage)
        holder.itemView.image.outlineProvider = ViewOutlineProvider.BACKGROUND
        holder.itemView.image.clipToOutline = true

    }

    override fun getItemCount(): Int {
        return images.size
    }
}