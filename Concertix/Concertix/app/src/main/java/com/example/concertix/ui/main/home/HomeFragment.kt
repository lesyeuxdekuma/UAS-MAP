package com.example.concertix.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.concertix.R
import com.example.concertix.Util.parseDate
import com.example.concertix.adapter.EventsAdapter
import com.example.concertix.databinding.CustomCarouselLayoutBinding
import com.example.concertix.databinding.FragmentHomeBinding
import com.example.concertix.model.Events
import com.example.concertix.ui.genre.GenreActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        adapter = EventsAdapter()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            binding.carousel.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return CustomCarouselLayoutBinding.inflate(layoutInflater, parent, false)
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
                    // Cast the binding to the returned view binding class of the onCreateViewHolder() method.
                    val currentBinding = binding as CustomCarouselLayoutBinding

                    // Do the bindings...
                    currentBinding.imageView.apply {
                        // setImage() is an extension function to load image to an ImageView using CarouselItem object. We need to provide current CarouselItem data and the place holder Drawable or drawable resource id to the function. placeholder parameter is optional.
                        setImage(item, item.imageDrawable!!)
                    }
                }
            }

            val imageCarouselList = listOf(
                CarouselItem(
                    imageDrawable = R.drawable.pestapora
                ),
                CarouselItem(
                    imageDrawable = R.drawable.wethefest
                ),
                CarouselItem(
                    imageDrawable = R.drawable.jazz
                )
            )

            with(binding) {
                carousel.setData(imageCarouselList)
                rvEvents.adapter = adapter

                val genreIntent = Intent(it, GenreActivity::class.java)
                llPop.setOnClickListener {
                    genreIntent.putExtra("genre", "Pop")
                    startActivity(genreIntent)
                }
                llHiphop.setOnClickListener {
                    genreIntent.putExtra("genre", "Hip-hop")
                    startActivity(genreIntent)
                }
                llDisco.setOnClickListener {
                    genreIntent.putExtra("genre", "Disco")
                    startActivity(genreIntent)
                }
                llVocal.setOnClickListener {
                    genreIntent.putExtra("genre", "Vocal")
                    startActivity(genreIntent)
                }
                llPopRock.setOnClickListener {
                    genreIntent.putExtra("genre", "Pop Rock")
                    startActivity(genreIntent)
                }
            }

            getEventsData()
        }
    }

    fun getEventsData() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}