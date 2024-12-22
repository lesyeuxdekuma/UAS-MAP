package com.example.concertix.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.concertix.R
import com.example.concertix.databinding.ActivityLandingPageBinding
import com.example.concertix.ui.login.LoginActivity

class LandingPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding
    private var currentFragment : Fragment = LandingPage1Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this) {
            if (currentFragment is LandingPage1Fragment) {
                this@LandingPageActivity.finishAffinity()
            } else {
                currentFragment = LandingPage1Fragment()
                supportFragmentManager.popBackStack()
                with(binding) {
                    tvTitle.text = getString(R.string.landing_page_1_bottom_sheet_title)
                    tvDescription.text = getString(R.string.landing_page_1_bottom_sheet_description)
                    btnNext.text = getString(R.string.landing_page_1_bottom_sheet_button_text)
                }
            }
        }

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment()

        binding.btnNext.setOnClickListener {
            if (currentFragment is LandingPage2Fragment) {
                startActivity(Intent(this@LandingPageActivity, LoginActivity::class.java))
            } else {
                currentFragment = LandingPage2Fragment()
                changeFragment()
                with(binding) {
                    tvTitle.text = getString(R.string.landing_page_2_bottom_sheet_title)
                    tvDescription.text = getString(R.string.landing_page_2_bottom_sheet_description)
                    btnNext.text = getString(R.string.landing_page_2_bottom_sheet_button_text)
                }
            }
        }
    }

    private fun changeFragment() {
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            replace(binding.container.id, currentFragment)
            addToBackStack(null)
        }
    }
}