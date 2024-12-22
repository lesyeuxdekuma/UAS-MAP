package com.example.concertix.ui.main.ticket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.concertix.R
import com.example.concertix.Util.parseDate
import com.example.concertix.adapter.AvailableEventsAdapter
import com.example.concertix.databinding.FragmentHomeBinding
import com.example.concertix.databinding.FragmentTicketBinding
import com.example.concertix.model.Events
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AvailableEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adapter = AvailableEventsAdapter()

        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            getData()
        }
    }

    fun getData() {
        binding.rvAvailableEvent.adapter = adapter

        Firebase.database.getReference("events").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val events = mutableListOf<Events>()
                for (ds in snapshot.children) {
                    val event = ds.getValue(Events::class.java)
                    if (event != null) {
                        events.add(event)
                    }
                }

                if (events.isNotEmpty()) {
                    val filtered = events.filter { it.date.parseDate() >= Date() }
                    filtered.sortedBy { it.date.parseDate() }
                    adapter.setData(events)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}