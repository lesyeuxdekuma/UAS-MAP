package com.example.concertix.ui.genre

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.concertix.adapter.GenreAdapter
import com.example.concertix.databinding.ActivityGenreBinding
import com.example.concertix.model.Events
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GenreActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGenreBinding
    private lateinit var ref : DatabaseReference
    private lateinit var tunggalAdapter : GenreAdapter
    private lateinit var festivalAdapter : GenreAdapter
    private var genre = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        genre = intent.getStringExtra("genre").orEmpty()

        ref = Firebase.database.reference

        binding.tvPageTitle.text = genre
        binding.btnBack.setOnClickListener {
            finish()
        }

        tunggalAdapter = GenreAdapter()
        binding.rvKonserTunggal.adapter = tunggalAdapter

        festivalAdapter = GenreAdapter()
        binding.rvFestival.adapter = festivalAdapter

        getData()
    }

    fun getData() {
        ref.child("events").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tunggalEvents = mutableListOf<Events>()
                val festivalEvents = mutableListOf<Events>()
                for (ds in snapshot.children) {
                    val event = ds.getValue(Events::class.java)
                    if (event != null && (event.genre == "All" || event.genre == genre)) {
                        if (event.eventType == "Konser Tunggal") {
                            tunggalEvents.add(event)
                        } else {
                            festivalEvents.add(event)
                        }
                    }
                }

                tunggalAdapter.setData(tunggalEvents)
                festivalAdapter.setData(festivalEvents)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}