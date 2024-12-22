package com.example.concertix.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.poslogistics.oneposlog.utils.ClearErrorTextWatcher
import com.example.concertix.LoadingDialog
import com.example.concertix.ui.main.MainActivity
import com.example.concertix.databinding.ActivityLoginBinding
import com.example.concertix.ui.signup.SignUpActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        with(binding) {
            tvSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            edtEmail.addTextChangedListener(ClearErrorTextWatcher(layoutEmail))
            edtPassword.addTextChangedListener(ClearErrorTextWatcher(layoutPassword))

            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                var isError = false

                if (email.isEmpty()) {
                    isError = true
                    layoutEmail.error = "Please fill your email"
                }

                if (password.isEmpty()) {
                    isError = true
                    layoutPassword.error = "Please fill your password"
                }

                if (isError) {
                    Toast.makeText(this@LoginActivity, "Please check your input", Toast.LENGTH_SHORT).show()
                } else {
                    login(email, password)
                }
            }
        }
    }

    fun login(email: String, password: String) {
        loadingDialog.show()
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                loadingDialog.dismiss()
                if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(this@LoginActivity, task.exception?.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}