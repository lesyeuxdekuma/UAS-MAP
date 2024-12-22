package com.example.concertix.ui.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.concertix.R
import com.example.concertix.databinding.ActivityMainBinding
import com.example.concertix.ui.main.home.HomeFragment
import com.example.concertix.ui.main.order.OrderFragment
import com.example.concertix.ui.main.profile.ProfileFragment
import com.example.concertix.ui.main.ticket.TicketFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    changeFragment(HomeFragment())
                    true
                }
                R.id.menu_ticket -> {
                    changeFragment(TicketFragment())
                    true
                }
                R.id.menu_order -> {
                    changeFragment(OrderFragment())
                    true
                }
                R.id.menu_profile -> {
                    changeFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        binding.navView.selectedItemId = R.id.menu_home
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(binding.container.id, fragment)
            addToBackStack(null)
        }
    }
}