package com.example.concertix.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.concertix.Util.formatDate
import com.example.concertix.databinding.ItemTransactionBinding
import com.example.concertix.model.Transaction
import com.example.concertix.ui.transactiondetail.TransactionDetailActivity

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    val transactions = mutableListOf<Transaction>()

    fun setData(transactionList: List<Transaction>) {
        transactions.clear()
        transactions.addAll(transactionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val txn = transactions[position]
        val context = holder.itemView.context
        with(holder.binding) {
            tvEventName.text = txn.eventName
            tvDate.text = txn.eventDate.formatDate()
            tvLocation.text = txn.eventPlace
            tvType.text = txn.ticketType
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TransactionDetailActivity::class.java)
            intent.putExtra("from_order", true)
            intent.putExtra("transaction", txn)
            context.startActivity(intent)
        }
    }

    class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)
}