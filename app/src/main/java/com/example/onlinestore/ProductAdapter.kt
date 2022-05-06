package com.example.onlinestore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val dataSet: ArrayList<Product>, private val activity: Activity, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name1: TextView = view.findViewById(R.id.name1)
        val description1: TextView = view.findViewById(R.id.description1)
        val rating1: TextView = view.findViewById(R.id.rating1)
        val reviewsCount1: TextView = view.findViewById(R.id.reviews_count1)
        val layout: LinearLayout = view.findViewById(R.id.layout)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name1.text = dataSet[position].name
        viewHolder.description1.text = dataSet[position].description
        viewHolder.rating1.text = dataSet[position].rating
        viewHolder.reviewsCount1.text = dataSet[position].reviewsCount

        viewHolder.layout.setOnClickListener{
            val intent = Intent(activity, ProductMoreActivity::class.java)
            intent.putExtra("productId", "90")
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size

}