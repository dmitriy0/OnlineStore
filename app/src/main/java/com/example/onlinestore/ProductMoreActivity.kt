package com.example.onlinestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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
        val textViewPrice = findViewById<TextView>(R.id.price)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val buttonAddDrop = findViewById<Button>(R.id.add_to_basket)

        val firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val productRef = database.getReference("products")
        val userRef = database.getReference("users")
        val productId = intent.getStringExtra("productId").toString()

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.child(firebaseAuth.uid.toString()).child("basket").child("count").value.toString().toInt()
                for (i in 0 until count) {
                     if (snapshot.child(firebaseAuth.uid.toString()).child("basket").child(i.toString()).value.toString() == productId) {
                         buttonAddDrop.text = "Удалить из корзины"
                     }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        buttonAddDrop.setOnClickListener {

            if (buttonAddDrop.text == "Добавить в корзину"){

                userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.child(firebaseAuth.uid.toString()).child("basket").child("count").value.toString().toInt()
                        userRef.child(firebaseAuth.uid.toString()).child("basket").child(count.toString()).setValue(productId)
                        userRef.child(firebaseAuth.uid.toString()).child("basket").child("count").setValue(count + 1)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
                buttonAddDrop.text = "Удалить из корзины"
            } else {
                userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.child(firebaseAuth.uid.toString()).child("basket").child("count").value.toString().toInt()
                        userRef.child(firebaseAuth.uid.toString()).child("basket").child((count-1).toString()).removeValue()
                        userRef.child(firebaseAuth.uid.toString()).child("basket").child("count").setValue(count - 1)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
                buttonAddDrop.text = "Добавить в корзину"
            }
        }

        productRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val name = snapshot.child(productId.toString()).child("name").value.toString()
                val description = snapshot.child(productId.toString()).child("description").value.toString()
                val price = snapshot.child(productId.toString()).child("price").value.toString()

                textViewName.text = name
                textViewDescription.text = description
                textViewPrice.text = price

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