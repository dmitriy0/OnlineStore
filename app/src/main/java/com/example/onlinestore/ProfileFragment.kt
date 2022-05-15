package com.example.onlinestore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val database = FirebaseDatabase.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val myRef = database.getReference("users")
        val textViewName = view.findViewById<TextView>(R.id.text_name)
        val buttonLogout = view.findViewById<Button>(R.id.log_out)
        buttonLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }
        val user = FirebaseAuth.getInstance().currentUser
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val name = snapshot.child(user!!.uid).child("name").value
                textViewName.text = name.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        return view
    }

}