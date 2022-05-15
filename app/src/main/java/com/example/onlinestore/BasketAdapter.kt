package com.example.onlinestore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BasketAdapter(private val dataSet: ArrayList<Product>, private val activity: Activity, private val context: Context) :
    RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val image: ImageView = view.findViewById(R.id.image)
        val price: TextView = view.findViewById(R.id.price)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.basket_recyclerview_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dataItem = dataSet[position]
        viewHolder.name.text = dataItem.name
        viewHolder.price.text = dataItem.price

        val storageRef = Firebase.storage.reference
        storageRef.child("products").child(dataItem.productId).child("0.jpg").downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(viewHolder.image)
        }

    }

    override fun getItemCount() = dataSet.size
}