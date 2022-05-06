package com.example.onlinestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firebaseAuth = FirebaseAuth.getInstance()
        //val button: Button = findViewById(R.id.button)

        //button.setOnClickListener(View.OnClickListener {
       //     firebaseAuth.signOut()
        //    intent = Intent(this, SignInActivity::class.java)
        //    startActivity(intent)
        //})

        supportFragmentManager.beginTransaction().replace(R.id.container,ProfileFragment()).commit()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()
            when(item.itemId){
                R.id.profile -> {
                    transaction.replace(R.id.container,ProfileFragment())
                    transaction.commit()
                    true
                }
                R.id.product_list ->{
                    transaction.replace(R.id.container, ProductFragment())
                    transaction.commit()
                    true
                }
                R.id.basket -> {
                    transaction.replace(R.id.container, BasketFragment())
                    transaction.commit()
                    true
                }
                else -> {false}
            }
        }

    }
}