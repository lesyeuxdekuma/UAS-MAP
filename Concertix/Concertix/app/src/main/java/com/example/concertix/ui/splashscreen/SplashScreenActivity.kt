package com.example.concertix.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.concertix.ui.main.MainActivity
import com.example.concertix.ui.landing.LandingPageActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplashScreen = true

        splashScreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashScreen = false
            if (getCurrentUser() != null) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                this@SplashScreenActivity.finish()
            } else {
                startActivity(Intent(this@SplashScreenActivity, LandingPageActivity::class.java))
                this@SplashScreenActivity.finish()
            }
        }, 2000)
    }

    private fun getCurrentUser() : FirebaseUser? {
        val auth = Firebase.auth
        return auth.currentUser
    }
}