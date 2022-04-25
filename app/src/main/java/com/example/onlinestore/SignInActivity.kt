package com.example.onlinestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonSignIn: Button = findViewById(R.id.button_sign_in)
        val emailInput: TextInputEditText = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        val textViewSignUp: Button = findViewById(R.id.button_sign_up)
        passwordInputLayout = findViewById(R.id.password_input_layout)
        emailInputLayout = findViewById(R.id.email_input_layout)

        buttonSignIn.setOnClickListener(View.OnClickListener {
            signIn(emailInput.text.toString(), passwordInput.text.toString())
        })

        textViewSignUp.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        })

        passwordInput.addTextChangedListener(TextFieldValidation(passwordInput))
        emailInput.addTextChangedListener(TextFieldValidation(emailInput))
    }


    private fun signIn(email: String, password: String){
        if (email != "" && password != "" && passwordInputLayout.error == null){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() {task ->
                    if (task.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener() {
                    passwordInput.setText("")
                    passwordInputLayout.error = "Wrong password"
                }
        }
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id){
                R.id.password_input -> {
                    passwordInputLayout.error = null
                    when {
                        s!!.isEmpty() -> {
                            passwordInputLayout.error = "Required field"
                        }
                        s.length < 6 -> {
                            passwordInputLayout.error = "Password can't be less then 6"
                        }
                        else -> {
                            passwordInputLayout.error = null
                        }
                    }
                }
                R.id.email_input -> {
                    if (s!!.isEmpty()) {
                        emailInputLayout.error = "Required field"
                    }
                    else {
                        emailInputLayout.error = null
                    }
                }
            }

        }
    }
}