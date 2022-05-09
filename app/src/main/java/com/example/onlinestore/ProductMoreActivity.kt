package com.example.onlinestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProductMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_more)

        val textViewName = findViewById<TextView>(R.id.name)
        val textViewDescription = findViewById<TextView>(R.id.description)
        val textViewRating = findViewById<TextView>(R.id.rating)
        val textViewReviewsCount = findViewById<TextView>(R.id.reviews_count)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products")
        val productId = intent.getStringExtra("productId")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val name = snapshot.child(productId.toString()).child("name").value.toString()
                val description = snapshot.child(productId.toString()).child("description").value.toString()
                val rating = snapshot.child(productId.toString()).child("rating").value.toString()
                val reviewsCount = snapshot.child(productId.toString()).child("reviews_count").value.toString()

                textViewName.text = name
                textViewDescription.text = description
                textViewRating.text = rating
                textViewReviewsCount.text = reviewsCount

                val storageRef = Firebase.storage.reference
                storageRef.child("products").child(productId.toString()).child("0.jpg").downloadUrl.addOnSuccessListener {
                    Glide.with(this@ProductMoreActivity).load(it).into(imageView)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}