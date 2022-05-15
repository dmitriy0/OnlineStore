package com.example.onlinestore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ProductFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_product, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val array = ArrayList<Product>()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products")

        myRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val count = snapshot.child("count").value.toString().toInt()
                for (i in 0 until count) {

                    val name = snapshot.child(i.toString()).child("name").value.toString()
                    val description = snapshot.child(i.toString()).child("description").value.toString()
                    val productId = snapshot.child(i.toString()).child("product_id").value.toString()
                    val price = snapshot.child(i.toString()).child("price").value.toString()
                    array.add(Product(name, description,
                        price,productId))
                    recyclerView.adapter = ProductAdapter(array, requireActivity(), requireContext())
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        return view
    }
}