package com.example.concertix.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.concertix.Util
import com.example.concertix.databinding.ItemEventsBinding
import com.example.concertix.model.Events
import com.example.concertix.ui.eventdetail.EventDetailActivity

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    val events = mutableListOf<Events>()

    fun setData(listEvents: List<Events>) {
        events.clear()
        events.addAll(listEvents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        val context = holder.itemView.context
        with(holder.binding) {
            tvEventName.text = event.eventName
            tvEventType.text = event.eventType

            Glide.with(holder.itemView.context)
                .load(event.eventBanner)
                .placeholder(Util.getCircularProgressDrawable(context))
                .into(ivBanner)

            btnView.setOnClickListener {
                val intent = Intent(context, EventDetailActivity::class.java)
                intent.putExtra("event", event)
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(val binding: ItemEventsBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}