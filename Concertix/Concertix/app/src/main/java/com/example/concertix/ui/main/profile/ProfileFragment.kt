package com.example.concertix.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.example.concertix.R
import com.example.concertix.databinding.FragmentProfileBinding
import com.example.concertix.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { ctx ->
            getCurrentUser()?.let { user ->
                Firebase.database.reference.child(user.uid).child("username").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val username = snapshot.getValue(String::class.java)
                        binding.tvUsername.text = username
                        binding.tvEmail.text = user.email
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }

            binding.btnSetting.setOnClickListener { view ->
                showPopupMenu(view)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_settings)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    performLogout()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun performLogout() {
        // Perform logout logic here (e.g., clear session, navigate to login screen)
        Firebase.auth.signOut()
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.let {
            it.finish()
        }
    }

    private fun getCurrentUser() : FirebaseUser? {
        val auth = Firebase.auth
        return auth.currentUser
    }
}