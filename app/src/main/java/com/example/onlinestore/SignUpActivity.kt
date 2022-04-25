package com.example.onlinestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText
    private lateinit var usernameInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val buttonSignUp: Button = findViewById(R.id.button_sign_up)
        val buttonSignIn: Button = findViewById(R.id.button_sign_in)
        val emailInput: TextInputEditText = findViewById(R.id.email_input)
        usernameInput = findViewById(R.id.username_input)
        val confirmPasswordInput: TextInputEditText = findViewById(R.id.confirm_password_input)
        passwordInput = findViewById(R.id.password_input)
        passwordInputLayout = findViewById(R.id.password_input_layout)
        emailInputLayout = findViewById(R.id.email_input_layout)
        usernameInputLayout = findViewById(R.id.username_input_layout)
        confirmPasswordInputLayout = findViewById(R.id.confirm_password_input_layout)

        buttonSignUp.setOnClickListener(View.OnClickListener {
            signUp(emailInput.text.toString(), passwordInput.text.toString(), confirmPasswordInput.text.toString())
        })

        buttonSignIn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        })

        passwordInput.addTextChangedListener(ValidateTextFields(passwordInput))
        confirmPasswordInput.addTextChangedListener(ValidateTextFields(confirmPasswordInput))
        usernameInput.addTextChangedListener(ValidateTextFields(usernameInput))
        emailInput.addTextChangedListener(ValidateTextFields(emailInput))

    }

    private fun signUp(email: String, password: String, confirmPassword: String) {
        if(email != "" && password != "" && confirmPassword != "" && passwordInputLayout.error == null && confirmPasswordInputLayout.error == null){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful){
                        val myRef = firebaseDatabase.getReference("users")
                        myRef.child(firebaseAuth.currentUser!!.uid).child("name").setValue(usernameInput.text.toString())
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }


        }

    }


    inner class ValidateTextFields(private val view: View): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id){
                R.id.password_input -> {
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
                R.id.confirm_password_input -> {
                    when {
                        s!!.isEmpty() -> {
                            confirmPasswordInputLayout.error = "Required field"
                        }

                        s.toString() != passwordInput.text.toString() -> {
                            confirmPasswordInputLayout.error = "Passwords don't match"
                        }
                        else -> {
                            confirmPasswordInputLayout.error = null
                        }

                    }
                }
                R.id.username_input -> {
                    if (s!!.isEmpty()) {
                        usernameInputLayout.error = "Required field"
                    }
                    else {
                        usernameInputLayout.error = null
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