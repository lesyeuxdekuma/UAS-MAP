package com.example.concertix.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.poslogistics.oneposlog.utils.ClearErrorTextWatcher
import com.example.concertix.LoadingDialog
import com.example.concertix.ui.main.MainActivity
import com.example.concertix.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)

        with(binding) {
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            tvLogin.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            edtUsername.addTextChangedListener(ClearErrorTextWatcher(layoutUsername))
            edtEmail.addTextChangedListener(ClearErrorTextWatcher(layoutEmail))
            edtPassword.addTextChangedListener(ClearErrorTextWatcher(layoutPassword))

            btnSignup.setOnClickListener {
                val username = edtUsername.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                var isError = false

                if (username.isEmpty()) {
                    isError = true
                    layoutUsername.error = "Please fill your username"
                }

                if (email.isEmpty()) {
                    isError = true
                    layoutEmail.error = "Please fill your email"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    isError = true
                    layoutEmail.error = "Invalid format"
                }

                if (password.isEmpty() || password.length < 6) {
                    isError = true
                    layoutPassword.error = "Password should be at least 6 characters"
                }

                if (isError) {
                    Toast.makeText(this@SignUpActivity, "Please check your input", Toast.LENGTH_SHORT).show()
                } else {
                    signup(username, email, password)
                }
            }
        }
    }

    fun signup(username: String, email: String, password: String) {
        loadingDialog.show()
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loadingDialog.dismiss()

            if (task.isSuccessful) {
                auth.currentUser?.let {
                    Firebase.database.reference.child(it.uid).child("username").setValue(username)
                }
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            } else {
                val exception = task.exception

                when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        Toast.makeText(this@SignUpActivity, "Email already used", Toast.LENGTH_SHORT).show()
                    }

                    is FirebaseAuthWeakPasswordException -> {
                        Toast.makeText(this@SignUpActivity, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(this@SignUpActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}