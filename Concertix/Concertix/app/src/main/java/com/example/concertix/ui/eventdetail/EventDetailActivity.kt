package com.example.concertix.ui.eventdetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.concertix.R
import com.example.concertix.Util
import com.example.concertix.Util.formatDate
import com.example.concertix.adapter.TicketAdapter
import com.example.concertix.databinding.ActivityEventDetailBinding
import com.example.concertix.databinding.LayoutSelectTicketBinding
import com.example.concertix.model.Events
import com.example.concertix.model.Ticket
import com.example.concertix.ui.payment.PaymentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val event = intent!!.getParcelableExtra<Events>("event")

        if(event != null) {
            with(binding) {
                Glide.with(this@EventDetailActivity)
                    .load(event.eventBanner)
                    .placeholder(Util.getCircularProgressDrawable(this@EventDetailActivity))
                    .into(ivBanner)

                tvEventName.text = event.eventName.toString()
                tvDate.text = event.date.formatDate()
                tvTime.text = "${event.startTime.toString()} - ${event.endTime.toString()}"
                tvLocation.text = event.placeName
                tvLocation2.text = event.placeLocation
                tvLocationDetail.text = event.placeName
                tvLocationDetail2.text = event.placeLocation
                tvEventType.text = event.eventType

                val lowPrice = event.tickets?.minByOrNull { it.price ?: 0L }?.price ?: 0L
                val highPrice = event.tickets?.maxByOrNull { it.price ?: 0L }?.price ?: 0L

                tvPrice.text = "${Util.formatCurrency(lowPrice)} - ${Util.formatCurrency(highPrice)}"
                tvPriceLow.text = "${Util.formatCurrency(lowPrice)} Onwards"


                btnBack.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }

                btnBuy.setOnClickListener {
                    showBottomSheet(event.tickets ?: emptyList(), event)
                }
            }
        }
    }

    fun showBottomSheet(ticket: List<Ticket>, event: Events) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.Theme_Concertix_BottomSheetDialog)
        val binding = LayoutSelectTicketBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(binding.root)

        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.background = resources.getDrawable(R.drawable.top_corner_radius_shape_blue, null)

        val ticketAdapter = TicketAdapter() {
            val intent = Intent(this@EventDetailActivity, PaymentActivity::class.java)
            intent.putExtra("event_name", event.eventName.toString())
            intent.putExtra("event_date", event.date.toString())
            intent.putExtra("event_place", event.placeName.toString())
            intent.putExtra("event_location", event.placeLocation.toString())
            intent.putExtra("event_time", "${event.startTime.toString()} - ${event.endTime.toString()}")
            intent.putExtra("event_type", event.eventType.toString())
            intent.putExtra("ticket", it)
            this@EventDetailActivity.startActivity(intent)
        }
        binding.rvTicket.apply {
            adapter = ticketAdapter
            layoutManager = LinearLayoutManager(this@EventDetailActivity)
            addItemDecoration(DividerItemDecoration(this@EventDetailActivity, LinearLayoutManager.VERTICAL))
        }
        ticketAdapter.setData(ticket)

        bottomSheetDialog.show()
    }
}