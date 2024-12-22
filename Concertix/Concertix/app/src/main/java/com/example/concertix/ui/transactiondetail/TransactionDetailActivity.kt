package com.example.concertix.ui.transactiondetail

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.concertix.R
import com.example.concertix.databinding.ActivityTransactionDetailBinding
import com.example.concertix.model.Transaction
import com.example.concertix.ui.main.MainActivity

class TransactionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionDetailBinding
    private var fromOrder = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = intent.getParcelableExtra<Transaction>("transaction")
        fromOrder = intent.getBooleanExtra("from_order", false)

        with(binding) {
            transaction?.let {
                tvTransactionId.text = "Transaction Number: ${it.id.toString()}"
                tvAmount.text = it.amount.toString()
                tvWallet.text = it.wallet.toString()
                tvEventName.text = it.eventName.toString()
                tvDate.text = it.eventDate.toString()
                tvTime.text = it.eventTime.toString()
                tvLocation.text = it.eventPlace.toString()
                tvLocation2.text = it.eventLocation.toString()
                tvEventType.text = it.eventType.toString()
            }

            btnDone.setOnClickListener {
                if (fromOrder) {
                    finish()
                } else {
                    val intent = Intent(this@TransactionDetailActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
        }
    }
}