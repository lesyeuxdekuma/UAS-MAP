package com.example.concertix.ui.main.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.concertix.Util.parseDate
import com.example.concertix.adapter.TransactionAdapter
import com.example.concertix.databinding.FragmentOrderBinding
import com.example.concertix.model.Transaction
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adapter = TransactionAdapter()
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            getCurrentUser()?.let {
                ref = Firebase.database.getReference(it.uid).child("transaction")
            }
            with(binding) {
                tab.addTab(tab.newTab().setText("Ongoing"))
                tab.addTab(tab.newTab().setText("Completed"))

                tab.addOnTabSelectedListener(object: OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when(tab?.position) {
                            0 -> getOngoing()
                            1 -> getCompleted()
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }
                })
                rvTransaction.adapter = adapter
            }

            getOngoing()
        }
    }

    fun getOngoing() {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = mutableListOf<Transaction>()
                for (ds in snapshot.children) {
                    val transaction = ds.getValue(Transaction::class.java)
                    if (transaction != null) {
                        transactions.add(transaction)
                    }
                }

                if (transactions.isNotEmpty()) {
                    val filtered = transactions.filter {
                        val parsedDate = it.eventDate.parseDate()
                        val currentDate = Date()
                        parsedDate >= currentDate
                    }
                    adapter.setData(filtered)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getCompleted() {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = mutableListOf<Transaction>()
                for (ds in snapshot.children) {
                    val transaction = ds.getValue(Transaction::class.java)
                    if (transaction != null) {
                        transactions.add(transaction)
                    }
                }

                if (transactions.isNotEmpty()) {
                    val filtered = transactions.filter {
                        val parsedDate = it.eventDate.parseDate()
                        val currentDate = Date()
                        parsedDate < currentDate
                    }
                    adapter.setData(filtered)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getCurrentUser() : FirebaseUser? {
        val auth = Firebase.auth
        return auth.currentUser
    }
}