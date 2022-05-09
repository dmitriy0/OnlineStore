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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProductAdapter(private val dataSet: ArrayList<Product>, private val activity: Activity, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name1: TextView = view.findViewById(R.id.name)
        val description1: TextView = view.findViewById(R.id.description)
        val rating1: TextView = view.findViewById(R.id.rating)
        val reviewsCount1: TextView = view.findViewById(R.id.reviews_count)
        val layout: LinearLayout = view.findViewById(R.id.layout)
        val image: ImageView = view.findViewById(R.id.image)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dataItem = dataSet[position]
        viewHolder.name1.text = dataItem.name
        viewHolder.description1.text = dataItem.description
        viewHolder.rating1.text = dataItem.rating
        viewHolder.reviewsCount1.text = dataItem.reviewsCount
        val storageRef = Firebase.storage.reference
        storageRef.child("products").child(dataItem.productId).child("0.jpg").downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(viewHolder.image)
        }

        viewHolder.layout.setOnClickListener{
            val intent = Intent(activity, ProductMoreActivity::class.java)
            intent.putExtra("productId", dataItem.productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size

}