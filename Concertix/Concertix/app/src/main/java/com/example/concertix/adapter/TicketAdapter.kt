package com.example.concertix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concertix.Util
import com.example.concertix.databinding.ItemTicketBinding
import com.example.concertix.model.Ticket

class TicketAdapter(
    val onAddClickListener: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    val tickets = mutableListOf<Ticket>()

    fun setData(listTicket: List<Ticket>) {
        tickets.clear()
        tickets.addAll(listTicket)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]

        with(holder.binding) {
            tvType.text = ticket.type.toString()
            tvDescription.text = ticket.description.toString()
            tvPrice.text = Util.formatCurrency(ticket.price ?: 0L)

            btnAdd.setOnClickListener {
                onAddClickListener(ticket)
            }
        }
    }

    class ViewHolder(val binding: ItemTicketBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}